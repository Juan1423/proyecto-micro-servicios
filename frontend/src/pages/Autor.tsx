export default function Autor() {
  const email = localStorage.getItem('email') || 'autor'
  return (
    <div style={{ padding: 20 }}>
      <h2>✍️ Panel Autor</h2>
      <p>Bienvenido, {email}.</p>

      <nav style={{ display: 'flex', gap: 10, marginTop: 10 }}>
        <a href="/mis-publicaciones">📄 Mis Publicaciones</a>
        <a href="/publicacion/nueva">➕ Crear Publicación</a>
        <a href="/catalogo">📚 Catálogo</a>
      </nav>
    </div>
  )
}
