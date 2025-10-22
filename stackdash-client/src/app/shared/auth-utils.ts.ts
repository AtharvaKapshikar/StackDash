    import { jwtDecode } from 'jwt-decode';

export class AuthUtils {}

export function getUserIdFromToken(): number | null {
  if (typeof window === 'undefined') return null;

  const token = localStorage.getItem('token');
  if (typeof token !== 'string' || !token.trim()) return null;

  try {
    const decoded: any = jwtDecode(token);
    return decoded?.userId ?? null;
  } catch (err) {
    console.error('Token decoding failed:', err);
    return null;
  }
}
