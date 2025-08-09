import { Router, Route } from 'preact-router'
import LoginForm from './pages/LoginForm'
import Dashboard from './pages/Dashboard'

import MisPublicaciones from './pages/autor/MisPublicaciones'
import EditarPublicacion from './pages/autor/EditarPublicacion'
import CrearPublicacion from './pages/autor/CrearPublicacion'

{/* Segunda Ventana Revisor */}
import PublicacionesAsignadas from './pages/revisor/PublicacionesAsignadas'
 {/*import RevisionComentarios from './pages/revisor/RevisionComentarios'*/}


{/* Tercera Ventana Editor */}
import GestionRevision from './pages/editor/GestionRevision'

{/* Roles */}
import Autor from './pages/Autor'
import Editor from './pages/Editor'
import Revisor from './pages/Revisor'


import CatalogoPublico from './pages/CatalogoPublico'

const App = () => {
  return (
    <Router>
      <Route path="/" component={LoginForm} />
      <Route path="/dashboard" component={Dashboard} />

      <Route path="/mis-publicaciones" component={MisPublicaciones} />
      <Route path="/publicacion/editar/:id" component={EditarPublicacion} />
      <Route path="/publicacion/nueva" component={CrearPublicacion} />

        {/* Segunda Ventana Revisor */}

       <Route path="/revisor/asignadas" component={PublicacionesAsignadas} />
        {/*<Route path="/revisor/comentar/:id" component={RevisionComentarios} />*/}

        {/* Tercera Ventana Editor */}
        <Route path="/editor/gestion" component={GestionRevision} />


        <Route path="/autor" component={Autor} />
         <Route path="/editor" component={Editor} />
      <Route path="/revisor" component={Revisor} />
     

     <Route path="/catalogo" component={CatalogoPublico} />

    </Router>
  )
}

export default App
