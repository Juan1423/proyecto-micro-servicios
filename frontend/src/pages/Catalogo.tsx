import { useEffect, useState } from 'react'

interface Publicacion {
  titulo: string
  autor: string
  tipo: string
  palabrasClave: string[]
}

const Catalogo = () => {
  const [publicaciones, setPublicaciones] = useState<Publicacion[]>([])

  useEffect(() => {
    // DATOS DE PRUEBA — NO BACKEND
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
    ]
    setPublicaciones(datosSimulados)
  }, [])

  return (
    <div style={{ padding: '20px' }}>
      <h2 style={{ textAlign: 'center' }}>Catálogo de Publicaciones</h2>
      <div
        style={{
          display: 'grid',
          gap: '20px',
          gridTemplateColumns: 'repeat(auto-fit, minmax(300px, 1fr))',
        }}
      >
        {publicaciones.map((pub, index) => (
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

export default Catalogo
