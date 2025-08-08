const Dashboard = () => {
  const rol = localStorage.getItem('rol')

  return (
    <div style={{ padding: '20px' }}>
      <h2>Bienvenido al Dashboard</h2>
      <p>Rol: {rol}</p>

      {rol === 'AUTOR' && <p><a href="/mis-publicaciones">Ver mis publicaciones</a></p>}
      {rol === 'REVISOR' && <p><a href="/revisor/asignadas">Publicaciones asignadas</a></p>}
      {rol === 'EDITOR' && <p><a href="/editor/gestionar">Gestionar revisiones</a></p>}
    </div>
  )
}

export default Dashboard
