import { useState } from 'preact/hooks'
import { route } from 'preact-router'

type Rol = 'AUTOR' | 'EDITOR' | 'REVISOR'
interface Usuario { email: string; password: string; rol: Rol }

const USERS: Usuario[] = [
  { email: 'autor@example.com',   password: 'Autor123!',   rol: 'AUTOR'   },
  { email: 'editor@example.com',  password: 'Editor123!',  rol: 'EDITOR'  },
  { email: 'revisor@example.com', password: 'Revisor123!', rol: 'REVISOR' },
]

export default function LoginForm() {
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [error, setError] = useState<string | null>(null)
  const [loading, setLoading] = useState(false)

  const goByRole = (rol: Rol) => {
    if (rol === 'AUTOR')   route('/autor')
    if (rol === 'EDITOR')  route('/editor')
    if (rol === 'REVISOR') route('/revisor')
  }

  const handleSubmit = (e: Event) => {
    e.preventDefault()
    setError(null)
    setLoading(true)

    // mock auth
    const user = USERS.find(u => u.email === email && u.password === password)

    setTimeout(() => {
      setLoading(false)
      if (!user) {
        setError('Credenciales inválidas')
        return
      }
      // guarda "sesión" mínima
      localStorage.setItem('token', 'demo-token')
      localStorage.setItem('rol', user.rol)
      localStorage.setItem('email', user.email)

      goByRole(user.rol)
    }, 500)
  }

  return (
    <form onSubmit={handleSubmit} style={{ maxWidth: 380, margin: '3rem auto', display: 'grid', gap: 10 }}>
      <h2>Iniciar sesión</h2>

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

      {error && <p style={{ color: 'crimson', margin: 0 }}>{error}</p>}

      <button type="submit" disabled={loading}>
        {loading ? 'Verificando…' : 'Entrar'}
      </button>

      <small style={{ opacity: 0.7 }}>
        Prueba: <br/>
        autor@example.com / Autor123!<br/>
        editor@example.com / Editor123!<br/>
        revisor@example.com / Revisor123!
      </small>
    </form>
  )
}
