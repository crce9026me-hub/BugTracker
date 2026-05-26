import { Link, useLocation, useNavigate } from 'react-router-dom'
import { Bug, LayoutDashboard, Ticket, FolderKanban, Users, LogOut, Search, Bell, Plus } from 'lucide-react'
import useAuthStore from '../../store/authStore'
import clsx from 'clsx'

const navItems = [
  { to: '/dashboard', icon: LayoutDashboard, label: 'Dashboard' },
  { to: '/tickets', icon: Ticket, label: 'Tickets' },
  { to: '/projects', icon: FolderKanban, label: 'Projects' },
]

export default function Layout({ children }) {
  const { pathname } = useLocation()
  const navigate = useNavigate()
  const { user, logout, isAdmin } = useAuthStore()

  const handleLogout = () => {
    logout()
    navigate('/login')
  }

  return (
    <div className="min-h-screen bg-slate-50">
      <div className="flex">
        <aside className="w-72 shrink-0 flex flex-col bg-slate-900 text-slate-100">
          <div className="px-6 py-6 border-b border-slate-800">
            <div className="flex items-center gap-3">
              <div className="h-10 w-10 rounded-2xl bg-sky-500 flex items-center justify-center shadow-sm">
                <Bug size={20} className="text-white" />
              </div>
              <div>
                <p className="text-sm uppercase tracking-[0.24em] text-slate-400">BugTracker</p>
                <p className="mt-1 text-lg font-semibold text-white">Issue Hub</p>
              </div>
            </div>
          </div>

          <div className="px-6 py-5">
            <div className="text-xs uppercase tracking-[0.24em] text-slate-500 mb-4">Navigation</div>
            <nav className="space-y-2">
              {navItems.map(({ to, icon: Icon, label }) => (
                <Link
                  key={to}
                  to={to}
                  className={clsx(
                    'group flex items-center gap-3 rounded-2xl px-4 py-3 text-sm font-medium transition',
                    pathname.startsWith(to)
                      ? 'bg-slate-800 text-white shadow-sm'
                      : 'text-slate-300 hover:bg-slate-800 hover:text-white'
                  )}
                >
                  <Icon size={18} className="text-slate-300 group-hover:text-white" />
                  <span>{label}</span>
                </Link>
              ))}

              {isAdmin() && (
                <Link
                  to="/users"
                  className={clsx(
                    'group flex items-center gap-3 rounded-2xl px-4 py-3 text-sm font-medium transition',
                    pathname.startsWith('/users')
                      ? 'bg-slate-800 text-white shadow-sm'
                      : 'text-slate-300 hover:bg-slate-800 hover:text-white'
                  )}
                >
                  <Users size={18} className="text-slate-300 group-hover:text-white" />
                  <span>Users</span>
                </Link>
              )}
            </nav>

            <div className="mt-6">
              <button
                onClick={() => navigate('/tickets/new')}
                className="flex w-full items-center justify-center gap-2 rounded-3xl bg-sky-500 px-4 py-3 text-sm font-semibold text-white shadow-sm transition hover:bg-sky-600"
              >
                <Plus size={16} />
                Create issue
              </button>
            </div>
          </div>

          <div className="mt-auto px-6 pb-6 pt-4 border-t border-slate-800">
            <div className="rounded-3xl border border-slate-700 bg-slate-950/70 p-4 shadow-sm">
              <div className="flex items-center gap-3">
                <div className="flex h-10 w-10 items-center justify-center rounded-2xl bg-sky-500 text-sm font-semibold text-white">
                  {user?.name?.charAt(0).toUpperCase()}
                </div>
                <div className="min-w-0">
                  <p className="truncate text-sm font-semibold text-white">{user?.name}</p>
                  <p className="truncate text-xs text-slate-500">{user?.role?.replace('_', ' ')}</p>
                </div>
              </div>
              <button
                onClick={handleLogout}
                className="mt-4 flex w-full items-center justify-center gap-2 rounded-2xl border border-slate-700 bg-slate-800 px-3 py-2 text-sm text-slate-200 hover:bg-slate-700"
              >
                <LogOut size={16} />
                Logout
              </button>
            </div>
          </div>
        </aside>

        <div className="flex-1">
          <header className="sticky top-0 z-20 border-b border-slate-200 bg-white/95 backdrop-blur-xl">
            <div className="mx-auto flex max-w-7xl items-center justify-between gap-4 px-6 py-4">
              <div className="relative w-full max-w-xl">
                <Search size={18} className="pointer-events-none absolute left-4 top-1/2 h-5 w-5 -translate-y-1/2 text-slate-400" />
                <input
                  className="h-12 w-full rounded-full border border-slate-200 bg-slate-50 pl-12 pr-4 text-sm text-slate-800 outline-none transition focus:border-sky-400 focus:ring-2 focus:ring-sky-100"
                  placeholder="Search issues, projects, users..."
                />
              </div>

              <div className="flex items-center gap-3">
                <button className="inline-flex h-12 w-12 items-center justify-center rounded-full bg-slate-100 text-slate-600 transition hover:bg-slate-200">
                  <Bell size={18} />
                </button>
                <div className="inline-flex items-center gap-3 rounded-full border border-slate-200 bg-white px-3 py-2 shadow-sm">
                  <span className="inline-flex h-10 w-10 items-center justify-center rounded-full bg-sky-500 text-xs font-semibold uppercase text-white">
                    {user?.name?.charAt(0).toUpperCase()}
                  </span>
                  <div className="min-w-0">
                    <p className="text-sm font-semibold text-slate-900">{user?.name}</p>
                    <p className="text-xs text-slate-500">{user?.role?.replace('_', ' ')}</p>
                  </div>
                </div>
              </div>
            </div>
          </header>

          <main>{children}</main>
        </div>
      </div>
    </div>
  )
}
