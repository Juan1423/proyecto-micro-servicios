import { useEffect, useMemo, useState } from 'preact/hooks'

/** Estados del flujo **/
type EstadoAutor = 'BORRADOR' | 'EN_REVISION' | 'APROBADO'
type EstadoRevisor = 'EN_REVISION' | 'APROBADO' | 'RECHAZADO' | 'CAMBIOS_SOLICITADOS'

/** Modelos simples **/
interface PubAutor {
  id: string
  titulo: string
  estado: EstadoAutor
}
interface PubRevisor {
  id: string
  titulo: string
  estado: EstadoRevisor
  comentario?: string
}
interface Asignaciones {
  [pubId: string]: string[]  // lista de emails de revisores
}

/** Claves de storage **/
const KEY_AUTOR = 'mis-publicaciones'
const KEY_REVISOR = 'revisor-asignadas'
const KEY_ASIG  = 'editor-asignaciones'
const KEY_CAT   = 'catalogo-publico' // cuando se aprueba, aparece en catálogo

export default function GestionRevision() {
  const [autorList, setAutorList] = useState<PubAutor[]>([])
  const [revisorList, setRevisorList] = useState<PubRevisor[]>([])
  const [asignaciones, setAsignaciones] = useState<Asignaciones>({})
  const [busqueda, setBusqueda] = useState('')
  const [nuevoRevisor, setNuevoRevisor] = useState<{[pubId: string]: string}>({})

  /* Cargar datos del storage */
  useEffect(() => {
    try {
      const a = JSON.parse(localStorage.getItem(KEY_AUTOR) || '[]') as PubAutor[]
      const r = JSON.parse(localStorage.getItem(KEY_REVISOR) || '[]') as PubRevisor[]
      const as = JSON.parse(localStorage.getItem(KEY_ASIG) || '{}') as Asignaciones
      setAutorList(a)
      setRevisorList(r)
      setAsignaciones(as)
    } catch {
      // noop
    }
  }, [])

  /** Publicaciones con estado EN_REVISION (las que interesan al editor) */
  const enRevision = useMemo(() => {
    const mapaAutor = new Map(autorList.map(p => [p.id, p]))
    // Si existen en lista del revisor, priorizamos su estado/comentario
    const combinadas = revisorList
      .filter(r => r.estado === 'EN_REVISION' || r.estado === 'CAMBIOS_SOLICITADOS' || r.estado === 'RECHAZADO' || r.estado === 'APROBADO')
      .map(r => ({
        id: r.id,
        titulo: r.titulo || (mapaAutor.get(r.id)?.titulo ?? 'Sin título'),
        estadoAutor: mapaAutor.get(r.id)?.estado ?? 'BORRADOR',
        estadoRevisor: r.estado,
        comentario: r.comentario
      }))

    // También incluir las que estén EN_REVISION del lado del autor aunque aún no estén en la bandeja del revisor
    const faltantes = autorList
      .filter(a => a.estado === 'EN_REVISION' && !combinadas.find(c => c.id === a.id))
      .map(a => ({
        id: a.id,
        titulo: a.titulo,
        estadoAutor: a.estado as EstadoAutor,
        estadoRevisor: 'EN_REVISION' as EstadoRevisor,
        comentario: undefined as string | undefined
      }))

    const result = [...combinadas, ...faltantes]
    const term = busqueda.trim().toLowerCase()
    if (!term) return result
    return result.filter(x => x.titulo.toLowerCase().includes(term))
  }, [autorList, revisorList, busqueda])

  /** Guardar asignaciones en storage */
  const persistAsignaciones = (next: Asignaciones) => {
    setAsignaciones(next)
    localStorage.setItem(KEY_ASIG, JSON.stringify(next))
  }

  const agregarRevisor = (pubId: string) => {
    const email = (nuevoRevisor[pubId] || '').trim()
    if (!email) return
    const cur = asignaciones[pubId] || []
    if (cur.includes(email)) {
      alert('Ese revisor ya está asignado.')
      return
    }
    const next = { ...asignaciones, [pubId]: [...cur, email] }
    persistAsignaciones(next)
    setNuevoRevisor({ ...nuevoRevisor, [pubId]: '' })
    alert('✅ Revisor asignado.')
  }

  const quitarRevisor = (pubId: string, email: string) => {
    const cur = asignaciones[pubId] || []
    const next = { ...asignaciones, [pubId]: cur.filter(x => x !== email) }
    persistAsignaciones(next)
  }

  /** Decisión final del editor */
  const decidir = (pubId: string, decision: 'APROBAR' | 'RECHAZAR') => {
    // 1) Reflejar en la lista del autor
    const nextAutor = autorList.map(a => {
      if (a.id !== pubId) return a
      if (decision === 'APROBAR') return { ...a, estado: 'APROBADO' as EstadoAutor }
      return { ...a, estado: 'BORRADOR' as EstadoAutor }
    })
    setAutorList(nextAutor)
    localStorage.setItem(KEY_AUTOR, JSON.stringify(nextAutor))

    // 2) Reflejar en la bandeja del revisor
    const nextRevisor = revisorList.map(r => {
      if (r.id !== pubId) return r
      if (decision === 'APROBAR') return { ...r, estado: 'APROBADO' as EstadoRevisor }
      return { ...r, estado: 'RECHAZADO' as EstadoRevisor }
    })
    localStorage.setItem(KEY_REVISOR, JSON.stringify(nextRevisor))
    setRevisorList(nextRevisor)

    // 3) Si se aprueba, publicar en "catálogo público" (MVP)
    if (decision === 'APROBAR') {
      const catRaw = localStorage.getItem(KEY_CAT)
      const cat = catRaw ? JSON.parse(catRaw) as {id:string; titulo:string}[] : []
      const titulo = nextAutor.find(a => a.id === pubId)?.titulo || 'Sin título'
      const actualizado = [{ id: pubId, titulo }, ...cat.filter(c => c.id !== pubId)]
      localStorage.setItem(KEY_CAT, JSON.stringify(actualizado))
    }

    alert(decision === 'APROBAR' ? '✅ Publicación aprobada y enviada al catálogo.' : '❌ Publicación rechazada (vuelve a borrador).')
  }

  return (
    <div style={{ padding: 20 }}>
      <h2>⚙️ Gestión de Revisión (Editor)</h2>

      <div style={{ display: 'flex', gap: 8, margin: '12px 0' }}>
        <input
          value={busqueda}
          onInput={(e) => setBusqueda((e.target as HTMLInputElement).value)}
          placeholder="Buscar por título…"
          style={{ padding: 8, minWidth: 260 }}
        />
        <button onClick={() => location.reload()}>🔄 Recargar</button>
      </div>

      {enRevision.length === 0 && (
        <p style={{ color: '#6b7280' }}>No hay publicaciones en revisión.</p>
      )}

      {enRevision.map((item) => (
        <div
          key={item.id}
          style={{
            border: '1px solid #e5e7eb',
            borderRadius: 10,
            padding: 12,
            marginBottom: 12,
            background: '#fff',
            boxShadow: '0 1px 2px rgba(0,0,0,0.05)',
          }}
        >
          <div style={{ display: 'flex', gap: 8, alignItems: 'center' }}>
            <h3 style={{ margin: 0, flex: 1 }}>{item.titulo}</h3>
            <small>Autor: <b>{item.estadoAutor}</b></small>
            <small>Revisor: <b>{item.estadoRevisor}</b></small>
          </div>

          {item.comentario && (
            <p style={{ marginTop: 8 }}>
              <strong>Comentario del revisor:</strong> {item.comentario}
            </p>
          )}

          {/* Asignaciones */}
          <div style={{ marginTop: 10 }}>
            <strong>Revisores asignados:</strong>{' '}
            {(asignaciones[item.id] && asignaciones[item.id].length > 0)
              ? asignaciones[item.id].join(', ')
              : <em>ninguno</em>}
          </div>

          <div style={{ display: 'flex', gap: 8, marginTop: 8, flexWrap: 'wrap' }}>
            <input
              type="email"
              placeholder="emaildelrevisor@ejemplo.com"
              value={nuevoRevisor[item.id] || ''}
              onInput={(e) => setNuevoRevisor({ ...nuevoRevisor, [item.id]: (e.target as HTMLInputElement).value })}
              style={{ padding: 6, minWidth: 260 }}
            />
            <button onClick={() => agregarRevisor(item.id)}>➕ Asignar</button>

            {(asignaciones[item.id] || []).map(email => (
              <button key={email} onClick={() => quitarRevisor(item.id, email)} title="Quitar revisor">
                🗑️ {email}
              </button>
            ))}
          </div>

          {/* Decisión final */}
          <div style={{ display: 'flex', gap: 10, marginTop: 12, flexWrap: 'wrap' }}>
            <button onClick={() => decidir(item.id, 'APROBAR')}>✅ Aprobar</button>
            <button onClick={() => decidir(item.id, 'RECHAZAR')}>❌ Rechazar</button>
            <a href="/revisor/asignadas">Ver en bandeja del revisor →</a>
          </div>
        </div>
      ))}
    </div>
  )
}





{/* Se inicia sesión como AUTOR, se crea una publicación y en Mis Publicaciones pulsa “Enviar a revisión” (el componente ya lo hace y cambia el estado).

Ve como REVISOR a /revisor/asignadas y deja un comentario o cambia a CAMBIOS_SOLICITADOS/APROBADO/RECHAZADO (opcional).

Entra como EDITOR a /editor/gestion:

Asigna uno o varios revisores por email.

Revisa el comentario del revisor (si lo hay).

Toma decisión final: Aprobar (pasa a APROBADO y se agrega al catálogo) o Rechazar (vuelve a BORRADOR para el autor).*/}