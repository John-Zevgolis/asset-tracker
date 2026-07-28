export type AssetCategory =
  | 'LAPTOP'
  | 'DESKTOP'
  | 'PHONE'
  | 'TABLET'
  | 'MONITOR'
  | 'OTHER';

export type Department =
  | 'IT'
  | 'HR'
  | 'FINANCE'
  | 'SALES'
  | 'MARKETING'
  | 'OPERATIONS'
  | 'LEGAL'
  | 'OTHER';

export interface AuthRequest {
  email: string;
  password: string;
}

export interface RegisterRequest {
  fullName: string;
  email: string;
  password: string;
}

export interface AuthResponse {
  token: string;
}

export interface AssetRequest {
  name: string;
  category: AssetCategory;
  serialNumber: string;
  value: number;
  purchaseDate: string;
}

export interface AssetResponse {
  id: number;
  name: string;
  category: AssetCategory;
  serialNumber: string;
  value: number;
  purchaseDate: string;
  employeeId: number | null;
  employeeName: string | null;
}

export interface EmployeeRequest {
  fullName: string;
  email: string;
  department: Department;
}

export interface EmployeeResponse {
  id: number;
  fullName: string;
  email: string;
  department: Department;
  assets: AssetResponse[];
}

export interface DashboardStats {
  totalAssets: number;
  totalAssetsValue: number;
  assignedAssetsCount: number;
  unassignedAssetsCount: number;
  totalEmployees: number;
}
