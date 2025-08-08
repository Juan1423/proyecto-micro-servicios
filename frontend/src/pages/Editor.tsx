export default function Editor() {
  const email = localStorage.getItem('email') || 'editor'
  return (
    <div style={{ padding: 20 }}>
      <h2>🧑‍⚖️ Panel Editor</h2>
      <p>Bienvenido, {email}.</p>

      <nav style={{ display: 'flex', gap: 10, marginTop: 10 }}>
        <a href="/editor/gestion">⚙️ Gestión de Revisión</a>
        <a href="/catalogo">📚 Catálogo Público</a>
      </nav>
    </div>
  )
}
