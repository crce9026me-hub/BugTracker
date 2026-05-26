import { create } from 'zustand';

const useSLAStore = create((set) => ({
  slaTracking: null,
  breaches: [],
  metrics: null,
  loading: false,
  error: null,

  setSLATracking: (slaTracking) => set({ slaTracking }),
  setBreaches: (breaches) => set({ breaches }),
  setMetrics: (metrics) => set({ metrics }),
  setLoading: (loading) => set({ loading }),
  setError: (error) => set({ error }),

  reset: () => set({
    slaTracking: null,
    breaches: [],
    metrics: null,
    loading: false,
    error: null,
  }),
}));

export default useSLAStore;
