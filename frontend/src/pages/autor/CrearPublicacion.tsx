import { useState } from 'preact/hooks'

interface Publicacion {
  id: string
  titulo: string
  estado: 'BORRADOR' | 'EN_REVISION' | 'APROBADO'
}

const CrearPublicacion = () => {
  const [titulo, setTitulo] = useState('')
  const [estado] = useState<'BORRADOR'>('BORRADOR') // siempre inicia en BORRADOR

  const guardar = () => {
    if (!titulo.trim()) {
      alert('❌ El título es obligatorio')
      return
    }

    const nuevaPublicacion: Publicacion = {
      id: Date.now().toString(),
      titulo: titulo.trim(),
      estado,
    }

    const raw = localStorage.getItem('mis-publicaciones')
    const publicaciones: Publicacion[] = raw ? JSON.parse(raw) : []
    publicaciones.push(nuevaPublicacion)

    localStorage.setItem('mis-publicaciones', JSON.stringify(publicaciones))
    alert('✅ Publicación creada en BORRADOR')
    window.location.href = '/mis-publicaciones'
  }

  return (
    <div style={{ padding: '20px' }}>
      <h2>Crear Nueva Publicación</h2>

      <div style={{ marginBottom: '10px' }}>
        <label>Título:</label>
        <input
          type="text"
          value={titulo}
          onInput={(e) => setTitulo((e.target as HTMLInputElement).value)}
          placeholder="Escribe el título..."
        />
      </div>

      <button onClick={guardar}>💾 Guardar</button>
    </div>
  )
}

export default CrearPublicacion
