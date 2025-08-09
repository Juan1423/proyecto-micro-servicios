import { useState } from 'preact/hooks'
import { route } from 'preact-router'
import { fetchApi } from '../api/apiClient'
import { jwtDecode } from 'jwt-decode' // Import jwtDecode

type Rol = 'ROLE_AUTOR' | 'ROLE_EDITOR' | 'ROLE_REVISOR' | 'ROLE_ADMIN' | 'ROLE_LECTOR'

interface LoginResponseData {
  token: string;
  role: Rol;
}

export default function LoginForm() {
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [error, setError] = useState<string | null>(null)
  const [loading, setLoading] = useState(false)

  const goByRole = (rol: Rol) => {
    if (rol === 'ROLE_AUTOR')   route('/autor')
    else if (rol === 'ROLE_EDITOR')  route('/editor')
    else if (rol === 'ROLE_REVISOR') route('/revisor')
    else if (rol === 'ROLE_ADMIN')   route('/admin')
    else if (rol === 'ROLE_LECTOR')  route('/lector')
    else route('/')
  }

  const handleSubmit = async (e: Event) => {
    e.preventDefault()
    setError(null)
    setLoading(true)

    try {
      const response = await fetchApi<LoginResponseData>('/auth/login', 'POST', { email, password }, false)
      const { token, role } = response

      if (token && role) {
        localStorage.setItem('token', token)
        localStorage.setItem('rol', role)
        localStorage.setItem('email', email)

        // Decode JWT to get user ID and store it
        try {
          const decodedToken: { sub: string } = jwtDecode(token);
          localStorage.setItem('userId', decodedToken.sub);
        } catch (decodeError) {
          console.error("Error decoding token:", decodeError);
          setError("Error al decodificar el token de usuario.");
          setLoading(false);
          return;
        }

        goByRole(role)
      } else {
        setError('Respuesta de autenticación inválida.')
      }
    } catch (err: any) {
      console.error('Error de autenticación:', err)
      if (err.response && err.response.data && err.response.data.message) {
        setError(err.response.data.message)
      } else if (err.message) {
        setError(err.message)
      } else {
        setError('Error al iniciar sesión. Inténtalo de nuevo.')
      }
    } finally {
      setLoading(false)
    }
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
