import { useState, useEffect } from 'preact/hooks'
import { route } from 'preact-router'

type Rol = 'AUTOR' | 'REVISOR'
interface Usuario { email: string; password: string; rol: Rol }

// Usuarios de prueba
const usuariosMock: Usuario[] = [
  { email: 'autor@example.com',   password: 'Autor123!',   rol: 'AUTOR'   },
  { email: 'revisor@example.com', password: 'Revisor123!', rol: 'REVISOR' }
]

export default function LoginForm() {
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [rol, setRol] = useState<Rol | null>(null)
  const [error, setError] = useState<string | null>(null)
  const [loading, setLoading] = useState(false)

  useEffect(() => {
    const savedRol = localStorage.getItem('rol') as Rol | null
    if (savedRol === 'AUTOR' || savedRol === 'REVISOR') {
      setRol(savedRol)
    }
  }, [])

  const handleLogin = (e: Event) => {
    e.preventDefault()
    setError(null)
    setLoading(true)

    const found = usuariosMock.find(
      u => u.email === email && u.password === password
    )

 setTimeout(() => {
  setLoading(false)
  if (!found) {
    setError('Credenciales inválidas')
    return
  }
  localStorage.setItem('rol', found.rol)
  setRol(found.rol)

  // 🔀 redirección por rol
  if (found.rol === 'AUTOR') {
    route('/mis-publicaciones')
  } else if (found.rol === 'REVISOR') {
    route('/revisor/asignadas')
  }
}, 500)

  const handleLogout = () => {
    localStorage.removeItem('rol')
    setRol(null)
    setEmail('')
    setPassword('')
  }

  // --- Si ya hay sesión ---
  if (rol) {
    return (
      <div style={{ padding: 16 }}>
        <header style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
          <strong>Sesión: {rol}</strong>
          <button onClick={handleLogout}>Salir</button>
        </header>

        {rol === 'AUTOR' && (
          <div>
            <h2>Panel del AUTOR ✍️</h2>
            <p>Aquí iría el contenido especial para autores.</p>
          </div>
        )}

        {rol === 'REVISOR' && (
          <div>
            <h2>Panel del REVISOR 🔎</h2>
            <p>Aquí iría el contenido especial para revisores.</p>
          </div>
        )}
      </div>
    )
  }

  // --- Si no hay sesión ---
  return (
    <form onSubmit={handleLogin} style={{ maxWidth: 360, margin: '2rem auto' }}>
      <h1>Iniciar sesión</h1>
      <label>Email</label>
      <input
        type="email"
        value={email}
        onInput={(e: any) => setEmail(e.currentTarget.value)}
        placeholder="autor@example.com"
        required
      />
      <label>Contraseña</label>
      <input
        type="password"
        value={password}
        onInput={(e: any) => setPassword(e.currentTarget.value)}
        placeholder="••••••••"
        required
      />
      {error && <p style={{ color: 'crimson' }}>{error}</p>}
      <button type="submit" disabled={loading}>
        {loading ? 'Verificando…' : 'Entrar'}
      </button>

      <div style={{ fontSize: 12, marginTop: 12, opacity: 0.7 }}>
        Prueba: autor@example.com / Autor123! — revisor@example.com / Revisor123!
      </div>
    </form>
  )
}}