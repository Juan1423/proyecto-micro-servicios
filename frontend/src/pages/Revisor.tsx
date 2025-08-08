export default function Revisor() {
  const email = localStorage.getItem('email') || 'revisor'
  return (
    <div style={{ padding: 20 }}>
      <h2>🔎 Panel Revisor</h2>
      <p>Bienvenido, {email}.</p>

      <nav style={{ display: 'flex', gap: 10, marginTop: 10 }}>
        <a href="/revisor/asignadas">📋 Publicaciones Asignadas</a>
        <a href="/catalogo">📚 Catálogo</a>
      </nav>
    </div>
  )
}
