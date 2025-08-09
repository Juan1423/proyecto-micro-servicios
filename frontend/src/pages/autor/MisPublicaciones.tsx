import { useEffect, useMemo, useState } from 'preact/hooks'
import { fetchApi } from '../../api/apiClient' // Import fetchApi

type Estado = 'BORRADOR' | 'EN_REVISION' | 'APROBADO' | 'CAMBIOS_SOLICITADOS' | 'PUBLICADO' | 'RETIRADO'

interface Publicacion {
  id: string
  titulo: string
  estado: Estado
  // Add other fields from backend Publicacion entity as needed
  resumen?: string
  palabrasClave?: string[]
  autorPrincipalId: string
  tipo?: string
  metadatos?: string
}

const MisPublicaciones = () => {
  const [publicaciones, setPublicaciones] = useState<Publicacion[]>([])
  const [q, setQ] = useState('')
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  const fetchPublicaciones = async () => {
    setLoading(true)
    setError(null)
    try {
      const userId = localStorage.getItem('userId');
      if (!userId) {
        setError('No se pudo encontrar el ID del usuario.');
        setLoading(false);
        return;
      }
      const data = await fetchApi<Publicacion[]>(`/publicaciones/autor/${userId}`, 'GET', undefined, true);
      setPublicaciones(data);
    } catch (err: any) {
      console.error('Error al cargar publicaciones:', err);
      setError('Error al cargar publicaciones. Consulta la consola para más detalles.');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchPublicaciones();
  }, []);

  const recargar = () => fetchPublicaciones();

  const onEnviarRevision = async (id: string) => {
    if (!confirm('¿Estás seguro de enviar esta publicación a revisión?')) return
    try {
      await fetchApi<Publicacion>(`/publicaciones/${id}/submit-for-review`, 'PUT', undefined, true);
      alert('📬 Publicación enviada a revisión.');
      fetchPublicaciones(); // Recargar la lista después de enviar a revisión
    } catch (err: any) {
      console.error('Error al enviar a revisión:', err);
      setError('Error al enviar a revisión. Consulta la consola para más detalles.');
    }
  };

  const onEliminar = async (id: string) => {
    if (!confirm('¿Estás seguro de eliminar esta publicación?')) return
    try {
      await fetchApi<void>(`/publicaciones/${id}`, 'DELETE', undefined, true);
      alert('🗑️ Publicación eliminada correctamente.');
      fetchPublicaciones(); // Recargar la lista después de eliminar
    } catch (err: any) {
      console.error('Error al eliminar publicación:', err);
      setError('Error al eliminar publicación. Consulta la consola para más detalles.');
    }
  };

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
      CAMBIOS_SOLICITADOS: { background: '#fefce8', color: '#854d0e' },
      PUBLICADO:    { background: '#dcfce7', color: '#16a34a' },
      RETIRADO:     { background: '#fee2e2', color: '#dc2626' },
    }
    return <span style={{ ...base, ...colors[estado] }}>{estado.replace('_', ' ')}</span>
  }

  if (loading) return <div style={{ padding: 20 }}>Cargando publicaciones...</div>
  if (error) return <div style={{ padding: 20, color: 'crimson' }}>Error: {error}</div>

  return (
    <div style={{ padding: 20 }}>
      <h2>Mis Publicaciones</h2>

      <div style={{ display: 'flex', gap: 8, margin: '12px 0' }}>
        <a href="/publicacion/nueva" style={{ display: 'inline-block' }}>
          ➕ Crear nueva publicación
        </a>
        <button onClick={recargar} disabled={loading}>🔄 Actualizar</button>
        <input
          placeholder="Buscar por título…"
          value={q}
          onInput={(e) => setQ((e.target as HTMLInputElement).value)}
          style={{ marginLeft: 'auto', padding: 8, minWidth: 220 }}
          disabled={loading}
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
              <button onClick={() => onEnviarRevision(pub.id)} disabled={loading}>📬 Enviar a revisión</button>
            )}

            {pub.estado === 'BORRADOR' && (
              <button onClick={() => onEliminar(pub.id)} style={{ color: '#b91c1c' }} disabled={loading}>
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
