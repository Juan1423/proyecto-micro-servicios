export async function fetchApi<T>(endpoint: string, method: string = 'GET', body?: object, authRequired: boolean = true): Promise<T> {
  const token = localStorage.getItem('token');
  const headers: HeadersInit = {
    'Content-Type': 'application/json',
  };

  if (authRequired && token) {
    headers['Authorization'] = `Bearer ${token}`;
  }

  const config: RequestInit = {
    method,
    headers,
  };

  if (body) {
    config.body = JSON.stringify(body);
  }

  const response = await fetch(`/api${endpoint}`, config);

  if (!response.ok) {
    const errorText = await response.text();
    throw new Error(errorText || `HTTP error! status: ${response.status}`);
  }

  // Si la respuesta no tiene contenido (ej. 204 No Content), no intentar parsear JSON
  if (response.status === 204) {
    return {} as T; // Devolver un objeto vacío o null, según lo que sea más apropiado para tu app
  }

  const contentType = response.headers.get('Content-Type');
  if (contentType && contentType.includes('application/json')) {
    return response.json();
  } else {
    // Assume it's plain text (e.g., a JWT string)
    return response.text() as Promise<T>; // Cast to T, assuming T can be string
  }
}
