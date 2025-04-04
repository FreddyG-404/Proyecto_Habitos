document.addEventListener("DOMContentLoaded", function () {
    const selectFiltro = document.getElementById("filtro-modulo");

    // Cargar todos los hábitos al inicio
    cargarTodosLosHabitos();

    // Escuchar cambios en el select de filtro
    selectFiltro.addEventListener("change", function () {
        cargarHabitos();
    });
});

const API_URL = "http://localhost:8080/api/habitos";
const TOKEN = localStorage.getItem("token");

function cargarHabitos() {
    if (!TOKEN) {
        alert("No estás autenticado");
        window.location.href = "/login.html";
        return;
    }

    const moduloSeleccionado = document.getElementById("filtro-modulo").value;
    let url = API_URL;

    if (moduloSeleccionado) {
        url += `/${moduloSeleccionado}`;
    }

    fetch(url, {
        method: "GET",
        headers: {
            "Authorization": `Bearer ${TOKEN}`,
            "Content-Type": "application/json"
        }
    })
    .then(response => {
        if (!response.ok) {
            throw new Error("Error al obtener los hábitos");
        }
        return response.json();
    })
    .then(habitos => {
        const lista = document.getElementById("habitos-list");
        lista.innerHTML = "";

        if (habitos.length === 0) {
            lista.innerHTML = "<p>No hay hábitos en este módulo.</p>";
            return;
        }

        habitos.forEach(habito => {
            const li = document.createElement("li");
            li.innerHTML = `
                ${habito.nombre} - ${habito.modulo} 
                <button class="btn completar" onclick="marcarCompletado(${habito.id})">
                    ${habito.completado ? "✔ Completado" : "Completar"}
                </button>
                <button class="btn eliminar" onclick="eliminarHabito(${habito.id})">❌ Eliminar</button>
            `;
            lista.appendChild(li);
        });
    })
    .catch(error => console.error("Error:", error));
}

document.getElementById("habito-form").addEventListener("submit", function (event) {
    event.preventDefault();

    const nombre = document.getElementById("habito-nombre").value;
    const modulo = document.getElementById("habito-modulo").value;

    fetch(API_URL, {
        method: "POST",
        headers: {
            "Authorization": `Bearer ${TOKEN}`,
            "Content-Type": "application/json"
        },
        body: JSON.stringify({ nombre, modulo })
    })
    .then(response => {
        if (!response.ok) {
            throw new Error("Error al agregar hábito");
        }
        return response.json();
    })
    .then(() => {
        cargarHabitos();
        document.getElementById("habito-form").reset();
    })
    .catch(error => console.error("Error:", error));
});

function marcarCompletado(id) {
    fetch(`${API_URL}/${id}/completar`, {
        method: "PUT",
        headers: {
            "Authorization": `Bearer ${TOKEN}`,
            "Content-Type": "application/json"
        }
    })
    .then(response => {
        if (!response.ok) {
            throw new Error("Error al marcar hábito como completado");
        }
        cargarHabitos();
    })
    .catch(error => console.error("Error:", error));
}

function eliminarHabito(id) {
    if (!confirm("¿Seguro que deseas eliminar este hábito?")) return;

    fetch(`${API_URL}/${id}`, {
        method: "DELETE",
        headers: {
            "Authorization": `Bearer ${TOKEN}`,
            "Content-Type": "application/json"
        }
    })
    .then(response => {
        if (!response.ok) {
            throw new Error("Error al eliminar hábito");
        }
        cargarHabitos();
    })
    .catch(error => console.error("Error:", error));
}

function cerrarSesion() {
    localStorage.removeItem("token");
    window.location.href = "login.html";
}
