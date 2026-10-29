export interface TokenSession { token: string; tipo: string; expiraEnSegundos: number; nombreCompleto: string; roles: string[]; permisos: string[] }
export interface Servicio { id: number; codigo: string; nombre: string; precio: number; duracionMin: number; activo: boolean }
export interface Cliente { id: number; documento: string; nombres: string; telefono?: string }
export interface Vehiculo { id: number; placa: string; marca: string; modelo: string; anio: number; clienteId: number; clienteNombre: string }
export interface DetalleOrden { servicioId: number; codigo: string; servicio: string; cantidad: number; precioUnitario: number; subtotal: number }
export interface Orden { id: number; numero: string; fecha: string; vehiculoId: number; placa: string; clienteNombre: string; kilometraje: number; estado: 'REGISTRADA' | 'ANULADA'; total: number; usuarioRegistro: string; motivoAnulacion?: string; detalles: DetalleOrden[] }
export interface ErrorApi { status?: number; mensaje?: string; campos?: Record<string, string>; error?: ErrorApi }
