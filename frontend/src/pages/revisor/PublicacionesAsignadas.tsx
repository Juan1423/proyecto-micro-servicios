import { useEffect, useState } from 'preact/hooks'

type EstadoRevision = 'EN_REVISION' | 'APROBADO' | 'RECHAZADO' | 'CAMBIOS_SOLICITADOS'

interface Publicacion {
  id: string
  titulo: string
  estado: EstadoRevision
  comentario?: string
}

const STORAGE_KEY = 'publicaciones-revisor'

const PublicacionesAsignadas = () => {
  const [publicaciones, setPublicaciones] = useState<Publicacion[]>([])
  const [comentarioActivo, setComentarioActivo] = useState<string>('')

  useEffect(() => {
    const raw = localStorage.getItem(STORAGE_KEY)
    if (raw) {
      setPublicaciones(JSON.parse(raw))
    } else {
      // Simulación de publicaciones asignadas
      const mock: Publicacion[] = [
        { id: '101', titulo: 'Inteligencia Artificial en la Salud', estado: 'EN_REVISION' },
        { id: '102', titulo: 'Blockchain en Educación', estado: 'EN_REVISION' },
      ]
      localStorage.setItem(STORAGE_KEY, JSON.stringify(mock))
      setPublicaciones(mock)
    }
  }, [])

  const actualizarEstado = (id: string, nuevoEstado: EstadoRevision, comentario?: string) => {
    const actualizadas = publicaciones.map((p) =>
      p.id === id ? { ...p, estado: nuevoEstado, comentario } : p
    )
    setPublicaciones(actualizadas)
    localStorage.setItem(STORAGE_KEY, JSON.stringify(actualizadas))
    alert(`✅ Publicación actualizada a: ${nuevoEstado}`)
  }

  return (
    <div style={{ padding: '20px' }}>
      <h2>📋 Publicaciones Asignadas</h2>

      {publicaciones.map((pub) => (
        <div
          key={pub.id}
          style={{
            border: '1px solid #ccc',
            borderRadius: '8px',
            padding: '10px',
            marginBottom: '10px',
            backgroundColor: '#fdfdfd',
          }}
        >
          <h3>{pub.titulo}</h3>
          <p><strong>Estado actual:</strong> {pub.estado}</p>

          <textarea
            rows={3}
            style={{ width: '100%', marginBottom: '8px' }}
            placeholder="Escribe un comentario..."
            onInput={(e) => setComentarioActivo((e.target as HTMLTextAreaElement).value)}
          />

          <div style={{ display: 'flex', gap: '8px' }}>
            <button onClick={() => actualizarEstado(pub.id, 'APROBADO', comentarioActivo)}>✅ Aprobar</button>
            <button onClick={() => actualizarEstado(pub.id, 'RECHAZADO', comentarioActivo)}>❌ Rechazar</button>
            <button onClick={() => actualizarEstado(pub.id, 'CAMBIOS_SOLICITADOS', comentarioActivo)}>✏️ Solicitar cambios</button>
          </div>

          {pub.comentario && (
            <p style={{ marginTop: '8px', color: '#555' }}>
              <strong>Comentario previo:</strong> {pub.comentario}
            </p>
          )}
        </div>
      ))}
    </div>
  )
}

export default PublicacionesAsignadas
