import { useState } from 'preact/hooks'
import { fetchApi } from '../../api/apiClient'

type Tipo = 'ARTICULO' | 'LIBRO' | 'OTRO'

interface Publicacion {
  id?: string
  titulo: string
  resumen?: string
  palabrasClave?: string[]
  estado: 'BORRADOR'
  autorPrincipalId: string
  coAutoresIds?: string[]
  tipo?: Tipo
  metadatos?: string // JSON en String
}

const CrearPublicacion = () => {
  const [titulo, setTitulo] = useState('')
  const [resumen, setResumen] = useState('')
  const [palabrasClaveInput, setPalabrasClaveInput] = useState('') // Comma separated string
  const [coAutoresIdsInput, setCoAutoresIdsInput] = useState('') // Comma separated UUIDs
  const [tipo, setTipo] = useState<Tipo>('ARTICULO')
  const [metadatosInput, setMetadatosInput] = useState('') // JSON string

  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)

  const guardar = async () => {
    if (!titulo.trim()) {
      alert('❌ El título es obligatorio')
      return
    }

    const autorPrincipalId = localStorage.getItem('userId')
    if (!autorPrincipalId) {
      setError('ID de autor no encontrado. Por favor, inicia sesión de nuevo.')
      return
    }

    setLoading(true)
    setError(null)

    try {
      const newPublicacion: Publicacion = {
        titulo: titulo,
        resumen: resumen.trim() === '' ? undefined : resumen,
        palabrasClave: palabrasClaveInput.trim() === '' ? undefined : palabrasClaveInput.split(',').map(s => s.trim()),
        estado: 'BORRADOR',
        autorPrincipalId: autorPrincipalId,
        coAutoresIds: coAutoresIdsInput.trim() === '' ? undefined : coAutoresIdsInput.split(',').map(s => s.trim()),
        tipo: tipo,
        metadatos: metadatosInput.trim() === '' ? undefined : metadatosInput,
      }

      await fetchApi<Publicacion>('/publicaciones', 'POST', newPublicacion, true)

      alert('✅ Publicación creada en BORRADOR')
      window.location.href = '/mis-publicaciones'
    } catch (err: any) {
      console.error('Error al crear publicación:', err)
      setError('Error al crear publicación. Consulta la consola para más detalles.')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div style={{ padding: '20px' }}>
      <h2>Crear Nueva Publicación</h2>

      {error && <p style={{ color: 'crimson' }}>{error}</p>}

      <div style={{ marginBottom: '10px' }}>
        <label>Título:</label>
        <input
          type="text"
          value={titulo}
          onInput={(e) => setTitulo((e.target as HTMLInputElement).value)}
          placeholder="Escribe el título..."
          disabled={loading}
        />
      </div>

      <div style={{ marginBottom: '10px' }}>
        <label>Resumen:</label>
        <textarea
          value={resumen}
          onInput={(e) => setResumen((e.target as HTMLTextAreaElement).value)}
          placeholder="Escribe un resumen..."
          disabled={loading}
        />
      </div>

      <div style={{ marginBottom: '10px' }}>
        <label>Palabras Clave (separadas por comas):</label>
        <input
          type="text"
          value={palabrasClaveInput}
          onInput={(e) => setPalabrasClaveInput((e.target as HTMLInputElement).value)}
          placeholder="ej. microservicios, arquitectura"
          disabled={loading}
        />
      </div>

      <div style={{ marginBottom: '10px' }}>
        <label>Co-Autores IDs (separadas por comas):</label>
        <input
          type="text"
          value={coAutoresIdsInput}
          onInput={(e) => setCoAutoresIdsInput((e.target as HTMLInputElement).value)}
          placeholder="ej. uuid1, uuid2"
          disabled={loading}
        />
      </div>

      <div style={{ marginBottom: '10px' }}>
        <label>Tipo:</label>
        <select
          value={tipo}
          onChange={(e) => setTipo((e.target as HTMLSelectElement).value as Tipo)}
          disabled={loading}
        >
          <option value="ARTICULO">Artículo</option>
          <option value="LIBRO">Libro</option>
          <option value="OTRO">Otro</option>
        </select>
      </div>

      <div style={{ marginBottom: '10px' }}>
        <label>Metadatos (JSON):</label>
        <textarea
          value={metadatosInput}
          onInput={(e) => setMetadatosInput((e.target as HTMLTextAreaElement).value)}
          placeholder="ej. { \doi\: \10.1234/example.1\ }"
          disabled={loading}
        />
      </div>

      <button onClick={guardar} disabled={loading}>
        {loading ? 'Guardando...' : '💾 Guardar'}
      </button>
    </div>
  )
}

export default CrearPublicacion
