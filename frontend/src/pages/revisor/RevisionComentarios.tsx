import { useEffect, useState } from 'preact/hooks'

type EstadoRevision = 'EN_REVISION' | 'APROBADO' | 'RECHAZADO' | 'CAMBIOS_SOLICITADOS'

interface Publicacion {
  id: string
  titulo: string
  estado: EstadoRevision
  comentario?: string
}

const KEY_REVISOR = 'revisor-asignadas'

interface Props {
  id?: string // se obtiene de la URL /revisor/comentar/:id
}

const RevisionComentarios = ({ id }: Props) => {
  const [pub, setPub] = useState<Publicacion | null>(null)
  const [comentario, setComentario] = useState('')

  useEffect(() => {
    if (!id) return
    const raw = localStorage.getItem(KEY_REVISOR)
    if (!raw) return
    const lista = JSON.parse(raw) as Publicacion[]
    const encontrada = lista.find((p) => p.id === id) || null
    setPub(encontrada)
    setComentario(encontrada?.comentario ?? '')
  }, [id])

  const actualizarEstado = (nuevoEstado: EstadoRevision) => {
    if (!pub) return
    const raw = localStorage.getItem(KEY_REVISOR)
    const lista = raw ? (JSON.parse(raw) as Publicacion[]) : []
    const actualizadas = lista.map((p) =>
      p.id === pub.id ? { ...p, estado: nuevoEstado, comentario } : p
    )
    localStorage.setItem(KEY_REVISOR, JSON.stringify(actualizadas))
    alert(`✅ Estado cambiado a: ${nuevoEstado}`)
    window.location.href = '/revisor/asignadas'
  }

  if (!pub) {
    return <p style={{ padding: 20 }}>No se encontró la publicación.</p>
  }

  return (
    <div style={{ padding: 20 }}>
      <h2>Revisión de Publicación</h2>
      <h3>{pub.titulo}</h3>
      <p><strong>Estado actual:</strong> {pub.estado}</p>

      <textarea
        rows={4}
        value={comentario}
        onInput={(e) => setComentario((e.target as HTMLTextAreaElement).value)}
        style={{ width: '100%', marginTop: 10 }}
        placeholder="Escribe un comentario..."
      />

      <div style={{ marginTop: 10, display: 'flex', gap: 10 }}>
        <button onClick={() => actualizarEstado('APROBADO')}>✅ Aprobar</button>
        <button onClick={() => actualizarEstado('RECHAZADO')}>❌ Rechazar</button>
        <button onClick={() => actualizarEstado('CAMBIOS_SOLICITADOS')}>✏️ Solicitar cambios</button>
      </div>
    </div>
  )
}

export default RevisionComentarios
