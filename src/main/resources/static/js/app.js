const elements = {
    list: document.querySelector("#loan-list"),
    empty: document.querySelector("#empty-state"),
    message: document.querySelector("#message"),
    total: document.querySelector("#total-count"),
    active: document.querySelector("#active-count"),
    late: document.querySelector("#late-count"),
    fine: document.querySelector("#fine-total"),
    refresh: document.querySelector("#refresh"),
    filters: document.querySelectorAll(".filter"),
    studentSearch: document.querySelector("#student-search"),
    studentId: document.querySelector("#student-id"),
    registerDialog: document.querySelector("#register-dialog"),
    registerForm: document.querySelector("#register-form"),
    renewDialog: document.querySelector("#renew-dialog"),
    renewForm: document.querySelector("#renew-form"),
    toast: document.querySelector("#toast")
};

let currentFilter = "all";
let toastTimer;

async function request(url, options = {}) {
    const response = await fetch(url, {
        headers: {
            "Content-Type": "application/json",
            ...options.headers
        },
        ...options
    });

    const text = await response.text();
    let data = null;

    if (text) {
        try {
            data = JSON.parse(text);
        } catch {
            data = text;
        }
    }

    if (!response.ok) {
        const detail = data?.detail || data?.message || formatValidationErrors(data);
        throw new Error(detail || `Erro ${response.status} ao realizar a operação.`);
    }

    return data;
}

function formatValidationErrors(data) {
    if (!data || typeof data !== "object" || Array.isArray(data)) {
        return "";
    }

    return Object.values(data)
        .filter(value => typeof value === "string")
        .join(" | ");
}

function formatDate(value) {
    if (!value) return "-";
    const [year, month, day] = value.split("-");
    return `${day}/${month}/${year}`;
}

function formatMoney(value) {
    return Number(value || 0).toLocaleString("pt-BR", {
        style: "currency",
        currency: "BRL"
    });
}

function safe(value, fallback = "-") {
    return value ?? fallback;
}

function getSituation(loan) {
    return loan.situacao || "SEM_PREVISAO";
}

function renderLoans(loans) {
    elements.list.innerHTML = "";
    elements.empty.classList.toggle("hidden", loans.length > 0);

    for (const loan of loans) {
        const situation = getSituation(loan);
        const row = document.createElement("tr");
        const canChange = situation !== "DEVOLVIDO";

        row.innerHTML = `
            <td><strong>#${safe(loan.id)}</strong></td>
            <td class="person">
                <strong>${safe(loan.aluno?.nome)}</strong>
                <small>ID ${safe(loan.aluno?.id)}</small>
            </td>
            <td class="book">
                <strong>${safe(loan.livro?.titulo)}</strong>
                <small>ID ${safe(loan.livro?.id)}</small>
            </td>
            <td>${formatDate(loan.dataEmprestimo)}</td>
            <td>${formatDate(loan.dataPrevistaDevolucao)}</td>
            <td><span class="badge ${situation.toLowerCase()}">${situation.replace("_", " ")}</span></td>
            <td>${safe(loan.diasAtraso, 0)} dia(s)</td>
            <td>${formatMoney(loan.valorMulta)}</td>
            <td>
                <div class="actions">
                    ${canChange ? `
                        <button class="button small secondary" data-action="renew" data-id="${loan.id}">Renovar</button>
                        <button class="button small danger" data-action="return" data-id="${loan.id}">Devolver</button>
                    ` : "-"}
                </div>
            </td>
        `;

        elements.list.appendChild(row);
    }
}

async function loadSummary() {
    const [all, active, late] = await Promise.all([
        request("/emprestimos"),
        request("/emprestimos/ativos"),
        request("/emprestimos/atrasados")
    ]);

    const openFine = late.reduce(
        (total, loan) => total + Number(loan.valorMulta || 0),
        0
    );

    elements.total.textContent = all.length;
    elements.active.textContent = active.length;
    elements.late.textContent = late.length;
    elements.fine.textContent = formatMoney(openFine);
}

async function loadLoans(filter = currentFilter) {
    currentFilter = filter;
    setLoading(true);
    hideMessage();

    const routes = {
        all: "/emprestimos",
        active: "/emprestimos/ativos",
        late: "/emprestimos/atrasados"
    };

    try {
        const loans = await request(routes[filter]);
        renderLoans(loans);
        await loadSummary();
    } catch (error) {
        showMessage(error.message);
        renderLoans([]);
    } finally {
        setLoading(false);
    }
}

function setLoading(isLoading) {
    elements.refresh.disabled = isLoading;
    elements.refresh.textContent = isLoading ? "Carregando..." : "Atualizar";
}

function showMessage(message) {
    elements.message.textContent = message;
    elements.message.classList.remove("hidden");
}

function hideMessage() {
    elements.message.classList.add("hidden");
}

function showToast(message, isError = false) {
    clearTimeout(toastTimer);
    elements.toast.textContent = message;
    elements.toast.classList.toggle("error", isError);
    elements.toast.classList.remove("hidden");

    toastTimer = setTimeout(() => {
        elements.toast.classList.add("hidden");
    }, 4000);
}

document.querySelector("#open-register").addEventListener("click", () => {
    elements.registerForm.reset();
    elements.registerDialog.showModal();
});

document.querySelectorAll(".close-dialog").forEach(button => {
    button.addEventListener("click", () => button.closest("dialog").close());
});

elements.registerForm.addEventListener("submit", async event => {
    event.preventDefault();

    const payload = {
        alunoId: Number(document.querySelector("#register-student").value),
        livroId: Number(document.querySelector("#register-book").value),
        dataPrevistaDevolucao: document.querySelector("#register-date").value
    };

    try {
        await request("/emprestimos", {
            method: "POST",
            body: JSON.stringify(payload)
        });

        elements.registerDialog.close();
        showToast("Empréstimo cadastrado com sucesso.");
        await loadLoans("all");
        activateFilter("all");
    } catch (error) {
        showToast(error.message, true);
    }
});

elements.renewForm.addEventListener("submit", async event => {
    event.preventDefault();

    const id = document.querySelector("#renew-id").value;
    const payload = {
        novaDataPrevista: document.querySelector("#renew-date").value
    };

    try {
        await request(`/emprestimos/${id}/renovacao`, {
            method: "PATCH",
            body: JSON.stringify(payload)
        });

        elements.renewDialog.close();
        showToast("Prazo renovado com sucesso.");
        await loadLoans();
    } catch (error) {
        showToast(error.message, true);
    }
});

elements.list.addEventListener("click", async event => {
    const button = event.target.closest("button[data-action]");
    if (!button) return;

    const id = button.dataset.id;

    if (button.dataset.action === "renew") {
        elements.renewForm.reset();
        document.querySelector("#renew-id").value = id;
        elements.renewDialog.showModal();
        return;
    }

    if (button.dataset.action === "return") {
        const confirmed = window.confirm(`Registrar a devolução do empréstimo ${id}?`);
        if (!confirmed) return;

        try {
            await request(`/emprestimos/${id}/devolucao`, { method: "PATCH" });
            showToast("Devolução registrada com sucesso.");
            await loadLoans();
        } catch (error) {
            showToast(error.message, true);
        }
    }
});

elements.filters.forEach(button => {
    button.addEventListener("click", () => {
        activateFilter(button.dataset.filter);
        loadLoans(button.dataset.filter);
    });
});

function activateFilter(filter) {
    elements.filters.forEach(button => {
        button.classList.toggle("active", button.dataset.filter === filter);
    });
}

elements.studentSearch.addEventListener("submit", async event => {
    event.preventDefault();
    const id = elements.studentId.value;

    setLoading(true);
    hideMessage();

    try {
        const loans = await request(`/emprestimos/aluno/${id}`);
        renderLoans(loans);
        elements.filters.forEach(button => button.classList.remove("active"));
        showToast(`Histórico do aluno ${id} carregado.`);
    } catch (error) {
        showMessage(error.message);
        renderLoans([]);
    } finally {
        setLoading(false);
    }
});

elements.refresh.addEventListener("click", () => {
    activateFilter(currentFilter);
    loadLoans(currentFilter);
});

loadLoans();
