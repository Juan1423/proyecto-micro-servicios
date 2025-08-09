import { useState, useEffect } from 'preact/hooks'
import { fetchApi } from '../../api/apiClient' // Import fetchApi

interface Publicacion {
  id: string
  titulo: string
  estado: 'BORRADOR' | 'EN_REVISION' | 'APROBADO' | 'CAMBIOS_SOLICITADOS' | 'PUBLICADO' | 'RETIRADO' // Extended states
  // Add other fields from backend Publicacion entity as needed
  resumen?: string
  palabrasClave?: string[]
  autorPrincipalId: string
  tipo?: string
  // ... other fields
}

interface Props {
  id?: string
}

const EditarPublicacion = ({ id }: Props) => {
  const [publicacion, setPublicacion] = useState<Publicacion | null>(null)
  const [titulo, setTitulo] = useState('')
  const [estado, setEstado] = useState<Publicacion['estado']>('BORRADOR')
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  useEffect(() => {
    const fetchPublicacion = async () => {
      if (!id) {
        setLoading(false)
        setError('ID de publicación no proporcionado.')
        return
      }
      try {
        // Fetch the full publication details from the backend
        const fetchedPub = await fetchApi<Publicacion>(`/publicaciones/${id}`, 'GET', undefined, true)
        setPublicacion(fetchedPub)
        setTitulo(fetchedPub.titulo)
        setEstado(fetchedPub.estado)
      } catch (err: any) {
        console.error('Error al cargar la publicación:', err)
        setError('Error al cargar la publicación.')
        // Fallback to local storage if backend fails (optional, for dev)
        const raw = localStorage.getItem('mis-publicaciones')
        if (raw) {
          const publicaciones: Publicacion[] = JSON.parse(raw)
          const pub = publicaciones.find((p) => p.id === id)
          if (pub) {
            setPublicacion(pub)
            setTitulo(pub.titulo)
            setEstado(pub.estado)
          }
        }
      } finally {
        setLoading(false)
      }
    }

    fetchPublicacion()
  }, [id])

  const guardarCambios = async () => {
    if (!publicacion) {
      setError('No hay publicación para guardar.')
      return
    }

    setLoading(true)
    setError(null)

    try {
      const updatedPublicacion = {
        ...publicacion,
        titulo: titulo,
        estado: estado,
      }
      // Send the updated full publication object to the backend
      await fetchApi<Publicacion>(`/publicaciones/${id}`, 'PUT', updatedPublicacion, true)
      alert('✅ Publicación actualizada correctamente')
      window.location.href = '/mis-publicaciones' // Redirect after successful update
    } catch (err: any) {
      console.error('Error al guardar cambios:', err)
      setError('Error al guardar cambios.')
    } finally {
      setLoading(false)
    }
  }

  if (loading) return <div style={{ padding: '20px' }}>Cargando...</div>
  if (error) return <div style={{ padding: '20px', color: 'crimson' }}>Error: {error}</div>
  if (!publicacion) return <div style={{ padding: '20px' }}>Publicación no encontrada.</div>

  return (
    <div style={{ padding: '20px' }}>
      <h2>Editar Publicación: {publicacion.titulo}</h2>

      <div style={{ marginBottom: '10px' }}>
        <label>Título:</label>
        <input
          type="text"
          value={titulo}
          onInput={(e) => setTitulo((e.target as HTMLInputElement).value)}
        />
      </div>

      <div style={{ marginBottom: '10px' }}>
        <label>Estado:</label>
        <select
          value={estado}
          onChange={(e) =>
            setEstado((e.target as HTMLSelectElement).value as Publicacion['estado'])
          }
        >
          <option value="BORRADOR">BORRADOR</option>
          <option value="EN_REVISION">EN REVISIÓN</option>
          <option value="APROBADO">APROBADO</option>
          <option value="CAMBIOS_SOLICITADOS">CAMBIOS SOLICITADOS</option>
          <option value="PUBLICADO">PUBLICADO</option>
          <option value="RETIRADO">RETIRADO</option>
        </select>
      </div>

      <button onClick={guardarCambios} disabled={loading}>💾 Guardar</button>
    </div>
  )
}

export default EditarPublicacion
