import { useEffect, useMemo, useState } from 'preact/hooks'

type Estado = 'BORRADOR' | 'EN_REVISION' | 'APROBADO'

interface Publicacion {
  id: string
  titulo: string
  estado: Estado
}

const STORAGE_KEY = 'mis-publicaciones'

function loadPubs(): Publicacion[] {
  try {
    const raw = localStorage.getItem(STORAGE_KEY)
    return raw ? JSON.parse(raw) as Publicacion[] : []
  } catch {
    return []
  }
}

function savePubs(pubs: Publicacion[]) {
  localStorage.setItem(STORAGE_KEY, JSON.stringify(pubs))
}

const MisPublicaciones = () => {
  const [publicaciones, setPublicaciones] = useState<Publicacion[]>([])
  const [q, setQ] = useState('')

  useEffect(() => {
    const raw = loadPubs()
    if (raw.length) {
      setPublicaciones(raw)
    } else {
      // semilla inicial si no hay nada
      const seed: Publicacion[] = [
        { id: '1', titulo: 'Uso de IA en Educación', estado: 'BORRADOR' },
        { id: '2', titulo: 'Arquitectura de Microservicios', estado: 'EN_REVISION' },
        { id: '3', titulo: 'Desarrollo Ágil con Scrum', estado: 'APROBADO' },
      ]
      savePubs(seed)
      setPublicaciones(seed)
    }
  }, [])

  const recargar = () => setPublicaciones(loadPubs())

  const onEnviarRevision = (id: string) => {
    const next = publicaciones.map(p =>
      p.id === id ? { ...p, estado: 'EN_REVISION' as Estado } : p
    )
    savePubs(next)
    setPublicaciones(next)
    alert('📬 Publicación enviada a revisión.')
  }

  const onEliminar = (id: string) => {
    if (!confirm('¿Eliminar este borrador?')) return
    const next = publicaciones.filter(p => p.id !== id)
    savePubs(next)
    setPublicaciones(next)
  }

  const filtradas = useMemo(() => {
    const term = q.trim().toLowerCase()
    if (!term) return publicaciones
    return publicaciones.filter(p => p.titulo.toLowerCase().includes(term))
  }, [q, publicaciones])

  const badge = (estado: Estado) => {
    const base: any = {
      padding: '2px 8px',
      borderRadius: '999px',
      fontSize: 12,
      fontWeight: 600,
    }
    const colors: Record<Estado, any> = {
      BORRADOR:     { background: '#eef2ff', color: '#3730a3' },
      EN_REVISION:  { background: '#fff7ed', color: '#9a3412' },
      APROBADO:     { background: '#ecfdf5', color: '#065f46' },
    }
    return <span style={{ ...base, ...colors[estado] }}>{estado.replace('_', ' ')}</span>
  }

  return (
    <div style={{ padding: 20 }}>
      <h2>Mis Publicaciones</h2>

      <div style={{ display: 'flex', gap: 8, margin: '12px 0' }}>
        <a href="/publicacion/nueva" style={{ display: 'inline-block' }}>
          ➕ Crear nueva publicación
        </a>
        <button onClick={recargar}>🔄 Actualizar</button>
        <input
          placeholder="Buscar por título…"
          value={q}
          onInput={(e) => setQ((e.target as HTMLInputElement).value)}
          style={{ marginLeft: 'auto', padding: 8, minWidth: 220 }}
        />
      </div>

      {filtradas.length === 0 && (
        <p style={{ color: '#6b7280' }}>No hay publicaciones que coincidan.</p>
      )}

      {filtradas.map((pub) => (
        <div
          key={pub.id}
          style={{
            border: '1px solid #e5e7eb',
            borderRadius: 10,
            padding: 12,
            marginBottom: 10,
            backgroundColor: '#fff',
            boxShadow: '0 1px 2px rgba(0,0,0,0.05)',
          }}
        >
          <div style={{ display: 'flex', alignItems: 'center', gap: 10 }}>
            <h3 style={{ margin: 0, flex: 1 }}>{pub.titulo}</h3>
            {badge(pub.estado)}
          </div>

          <div style={{ display: 'flex', gap: 10, marginTop: 8, flexWrap: 'wrap' }}>
            <a href={`/publicacion/editar/${pub.id}`}>✏️ Editar</a>

            {pub.estado === 'BORRADOR' && (
              <button onClick={() => onEnviarRevision(pub.id)}>📬 Enviar a revisión</button>
            )}

            {pub.estado === 'BORRADOR' && (
              <button onClick={() => onEliminar(pub.id)} style={{ color: '#b91c1c' }}>
                🗑️ Eliminar
              </button>
            )}
          </div>
        </div>
      ))}
    </div>
  )
}

export default MisPublicaciones
