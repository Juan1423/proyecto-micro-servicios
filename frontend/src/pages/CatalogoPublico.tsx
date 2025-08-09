import { useEffect, useMemo, useState } from 'preact/hooks'
import { fetchApi } from '../api/apiClient' // Import fetchApi

type Tipo = 'ARTICULO' | 'LIBRO' | 'OTRO'

interface ItemCatalogo {
  id: string
  titulo: string
  tipo?: Tipo
  autor?: string
  palabrasClave?: string[]   // opcional
  resumen?: string           // opcional
}

const KEY_CAT = 'catalogo-publico'

const CatalogoPublico = () => {
  const [items, setItems] = useState<ItemCatalogo[]>([])
  const [q, setQ] = useState('')               // búsqueda por título
  const [tipo, setTipo] = useState<Tipo | 'TODOS'>('TODOS')
  const [tag, setTag] = useState('')           // filtro por palabra clave

  // Cargar datos del backend o usar semilla de ejemplo
  useEffect(() => {
    const loadCatalog = async () => {
      try {
        const backendItems = await fetchApi<ItemCatalogo[]>('/catalogo?page=0&size=5', 'GET', undefined, true); // authRequired: true
        if (backendItems && backendItems.length > 0) {
          setItems(backendItems);
          localStorage.setItem(KEY_CAT, JSON.stringify(backendItems)); // Update local storage with backend data
        } else {
          // Fallback to local storage or seed if backend returns empty or null
          const raw = localStorage.getItem(KEY_CAT);
          if (raw) {
            setItems(JSON.parse(raw));
          } else {
            const seed: ItemCatalogo[] = [
              { id: 'cat-1', titulo: 'Arquitectura de Microservicios', tipo: 'ARTICULO', autor: 'Equipo ESPE', palabrasClave: ['microservicios','eureka','gateway'], resumen: 'Buenas prácticas y patrones.' },
              { id: 'cat-2', titulo: 'Libro: Ingeniería de Software Moderna', tipo: 'LIBRO', autor: 'A. García', palabrasClave: ['diseño','DDD','patrones'] },
              { id: 'cat-3', titulo: 'Observabilidad en Sistemas Distribuidos', tipo: 'ARTICULO', autor: 'M. Torres', palabrasClave: ['prometheus','jaeger','telemetría'] },
            ];
            localStorage.setItem(KEY_CAT, JSON.stringify(seed));
            setItems(seed);
          }
        }
      } catch (error) {
        console.error('Error fetching catalog from backend:', error);
        // Fallback to local storage or seed on error
        const raw = localStorage.getItem(KEY_CAT);
        if (raw) {
          setItems(JSON.parse(raw));
        } else {
          const seed: ItemCatalogo[] = [
            { id: 'cat-1', titulo: 'Arquitectura de Microservicios', tipo: 'ARTICULO', autor: 'Equipo ESPE', palabrasClave: ['microservicios','eureka','gateway'], resumen: 'Buenas prácticas y patrones.' },
            { id: 'cat-2', titulo: 'Libro: Ingeniería de Software Moderna', tipo: 'LIBRO', autor: 'A. García', palabrasClave: ['diseño','DDD','patrones'] },
            { id: 'cat-3', titulo: 'Observabilidad en Sistemas Distribuidos', tipo: 'ARTICULO', autor: 'M. Torres', palabrasClave: ['prometheus','jaeger','telemetría'] },
          ];
          localStorage.setItem(KEY_CAT, JSON.stringify(seed));
          setItems(seed);
        }
      }
    };

    loadCatalog();
  }, []);

  const filtrados = useMemo(() => {
    const term = q.trim().toLowerCase()
    const tagTerm = tag.trim().toLowerCase()
    return items.filter(it => {
      // filtro por tipo
      if (tipo !== 'TODOS' && it.tipo !== tipo) return false
      // búsqueda por título
      if (term && !it.titulo.toLowerCase().includes(term)) return false
      // filtro por palabra clave
      if (tagTerm) {
        const hay = (it.palabrasClave || []).some(p => p.toLowerCase().includes(tagTerm))
        if (!hay) return false
      }
      return true
    })
  }, [items, q, tipo, tag])

  return (
    <div style={{ padding: 20 }}>
      <h2>📚 Catálogo de Publicaciones</h2>

      {/* Filtros */}
      <div style={{ display: 'grid', gap: 8, gridTemplateColumns: '1fr 200px 220px', maxWidth: 900 }}>
        <input
          placeholder="🔎 Buscar por título…"
          value={q}
          onInput={(e) => setQ((e.target as HTMLInputElement).value)}
          style={{ padding: 8 }}
        />
        <select value={tipo} onChange={(e) => setTipo((e.target as HTMLSelectElement).value as any)} style={{ padding: 8 }}>
          <option value="TODOS">Todos los tipos</option>
          <option value="ARTICULO">Artículo</option>
          <option value="LIBRO">Libro</option>
          <option value="OTRO">Otro</option>
        </select>
        <input
          placeholder="🏷️ Palabra clave (ej. microservicios)"
          value={tag}
          onInput={(e) => setTag((e.target as HTMLInputElement).value)}
          style={{ padding: 8 }}
        />
      </div>

      {/* Resultados */}
      <p style={{ marginTop: 10, color: '#6b7280' }}>
        Resultados: <strong>{filtrados.length}</strong>
      </p>

      {filtrados.map(pub => (
        <article
          key={pub.id}
          style={{
            border: '1px solid #e5e7eb',
            borderRadius: 10,
            padding: 12,
            marginBottom: 10,
            background: '#fff',
            boxShadow: '0 1px 2px rgba(0,0,0,0.05)',
            maxWidth: 900
          }}
        >
          <header style={{ display: 'flex', gap: 8, alignItems: 'baseline', flexWrap: 'wrap' }}>
            <h3 style={{ margin: 0, flex: 1 }}>{pub.titulo}</h3>
            <span style={{ fontSize: 12, padding: '2px 8px', background: '#eef2ff', borderRadius: 999 }}>
              {pub.tipo || 'OTRO'}
            </span>
          </header>

          {pub.autor && <p style={{ margin: '6px 0' }}><strong>Autor:</strong> {pub.autor}</p>}
          {pub.resumen && <p style={{ margin: '6px 0' }}>{pub.resumen}</p>}

          {(pub.palabrasClave && pub.palabrasClave.length > 0) && (
            <div style={{ display: 'flex', flexWrap: 'wrap', gap: 6, marginTop: 6 }}>
              {pub.palabrasClave.map((p, i) => (
                <span key={i} style={{ fontSize: 12, background: '#f3f4f6', padding: '2px 6px', borderRadius: 6 }}>
                  #{p}
                </span>
              ))}
            </div>
          )}
        </article>
      ))}

      {filtrados.length === 0 && (
        <p style={{ color: '#6b7280' }}>No se encontraron publicaciones con esos criterios.</p>
      )}
    </div>
  )
}

export default CatalogoPublico
