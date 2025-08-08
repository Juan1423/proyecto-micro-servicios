import { useState, useEffect } from 'preact/hooks'

interface Publicacion {
  titulo: string
  autor: string
  tipo: string
  palabrasClave: string[]
}

const CatalogPage = () => {
  // Estado para las publicaciones y los filtros de búsqueda
  const [publicaciones, setPublicaciones] = useState<Publicacion[]>([])
  const [search, setSearch] = useState<string>('')  // Búsqueda por título o autor
  const [autorFilter, setAutorFilter] = useState<string>('')  // Filtro por autor
  const [tipoFilter, setTipoFilter] = useState<string>('')  // Filtro por tipo
  const [keywordsFilter, setKeywordsFilter] = useState<string>('')  // Filtro por palabras clave

  // Simulación de las publicaciones (sin backend)
  useEffect(() => {
    const datosSimulados = [
      {
        titulo: 'Microservicios Avanzados',
        autor: 'Andrea Ruiz',
        tipo: 'LIBRO',
        palabrasClave: ['Arquitectura', 'Spring Boot'],
      },
      {
        titulo: 'Aprendiendo React',
        autor: 'Carlos Mejía',
        tipo: 'ARTICULO',
        palabrasClave: ['Frontend', 'JavaScript', 'UI'],
      },
      {
        titulo: 'Introducción a TypeScript',
        autor: 'Laura González',
        tipo: 'ARTICULO',
        palabrasClave: ['TypeScript', 'JavaScript', 'Programación'],
      },
      {
        titulo: 'Fundamentos de Docker',
        autor: 'Pedro López',
        tipo: 'LIBRO',
        palabrasClave: ['Docker', 'Contenedores', 'DevOps'],
      },
    ]
    setPublicaciones(datosSimulados)
  }, [])

  // Función para filtrar las publicaciones según los criterios
  const filteredPublicaciones = publicaciones.filter((pub) => {
    const matchesSearch = pub.titulo.toLowerCase().includes(search.toLowerCase()) || pub.autor.toLowerCase().includes(search.toLowerCase())
    const matchesAutor = autorFilter ? pub.autor.toLowerCase().includes(autorFilter.toLowerCase()) : true
    const matchesTipo = tipoFilter ? pub.tipo.toLowerCase() === tipoFilter.toLowerCase() : true
    const matchesKeywords = keywordsFilter
      ? pub.palabrasClave.some((keyword) => keyword.toLowerCase().includes(keywordsFilter.toLowerCase()))
      : true

    return matchesSearch && matchesAutor && matchesTipo && matchesKeywords
  })

  return (
    <div style={{ padding: '20px' }}>
      <h2 style={{ textAlign: 'center' }}>Catálogo de Publicaciones</h2>

      {/* Filtros de búsqueda */}
      <div style={{ marginBottom: '20px' }}>
        <input
          type="text"
          placeholder="Buscar por título o autor"
          value={search}
          onInput={(e) => setSearch((e.target as HTMLInputElement).value)}
          style={{ marginRight: '10px' }}
        />
        <input
          type="text"
          placeholder="Filtrar por autor"
          value={autorFilter}
          onInput={(e) => setAutorFilter((e.target as HTMLInputElement).value)}
          style={{ marginRight: '10px' }}
        />
        <select
          value={tipoFilter}
          onChange={(e) => setTipoFilter((e.target as HTMLSelectElement).value)}
          style={{ marginRight: '10px' }}
        >
          <option value="">Filtrar por tipo</option>
          <option value="LIBRO">Libro</option>
          <option value="ARTICULO">Artículo</option>
        </select>
        <input
          type="text"
          placeholder="Filtrar por palabras clave"
          value={keywordsFilter}
          onInput={(e) => setKeywordsFilter((e.target as HTMLInputElement).value)}
          style={{ marginRight: '10px' }}
        />
      </div>

      {/* Mostrar las publicaciones filtradas */}
      <div
        style={{
          display: 'grid',
          gap: '20px',
          gridTemplateColumns: 'repeat(auto-fit, minmax(300px, 1fr))',
        }}
      >
        {filteredPublicaciones.map((pub, index) => (
          <div
            key={index}
            style={{
              border: '1px solid #ccc',
              borderRadius: '8px',
              padding: '15px',
              backgroundColor: '#f9f9f9',
              boxShadow: '0 2px 6px rgba(0,0,0,0.1)',
            }}
          >
            <h3>{pub.titulo}</h3>
            <p><strong>Autor:</strong> {pub.autor}</p>
            <p><strong>Tipo:</strong> {pub.tipo}</p>
            <p><strong>Palabras clave:</strong> {pub.palabrasClave.join(', ')}</p>
          </div>
        ))}
      </div>
    </div>
  )
}

export default CatalogPage
