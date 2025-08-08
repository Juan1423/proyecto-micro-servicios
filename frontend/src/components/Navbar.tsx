import { Link } from 'preact-router';

const Navbar = () => {
  return (
    <nav>
      <Link href="/login">Iniciar sesión</Link>
      <Link href="/catalogo">Catálogo</Link>
    </nav>
  );
};

export default Navbar;
