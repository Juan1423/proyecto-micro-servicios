import { useState, useEffect } from 'preact/hooks'

interface Publicacion {
  id: string
  titulo: string
  estado: 'BORRADOR' | 'EN_REVISION' | 'APROBADO'
}

interface Props {
  id?: string
}

const EditarPublicacion = ({ id }: Props) => {
  const [titulo, setTitulo] = useState('')
  const [estado, setEstado] = useState<'BORRADOR' | 'EN_REVISION' | 'APROBADO'>('BORRADOR')

  useEffect(() => {
    const raw = localStorage.getItem('mis-publicaciones')
    if (raw && id) {
      const publicaciones: Publicacion[] = JSON.parse(raw)
      const pub = publicaciones.find((p) => p.id === id)
      if (pub) {
        setTitulo(pub.titulo)
        setEstado(pub.estado)
      }
    }
  }, [id])

  const guardarCambios = () => {
    const raw = localStorage.getItem('mis-publicaciones')
    if (raw && id) {
      const publicaciones: Publicacion[] = JSON.parse(raw)
      const actualizadas = publicaciones.map((p) =>
        p.id === id ? { ...p, titulo, estado } : p
      )
      localStorage.setItem('mis-publicaciones', JSON.stringify(actualizadas))
      alert('✅ Publicación actualizada correctamente')
      window.location.href = '/mis-publicaciones'
    }
  }

  return (
    <div style={{ padding: '20px' }}>
      <h2>Editar Publicación</h2>

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
        </select>
      </div>

      <button onClick={guardarCambios}>💾 Guardar</button>
    </div>
  )
}

export default EditarPublicacion
