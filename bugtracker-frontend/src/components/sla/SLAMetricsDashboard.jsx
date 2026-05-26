import React, { useState, useEffect } from 'react';
import { BarChart, Bar, LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer } from 'recharts';
import { AlertTriangle } from 'lucide-react';
import slaApi from '../../services/slaApi';

const SLAMetricsDashboard = ({ projectId }) => {
  const [metrics, setMetrics] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    fetchMetrics();
    const interval = setInterval(fetchMetrics, 300000); // Refresh every 5 minutes
    return () => clearInterval(interval);
  }, [projectId]);

  const fetchMetrics = async () => {
    try {
      const data = await slaApi.getProjectMetrics(projectId);
      setMetrics(data);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  if (loading) return <div className="text-gray-500">Loading metrics...</div>;
  if (error) return <div className="text-red-500">Error: {error}</div>;
  if (!metrics) return null;

  const breachChartData = [
    {
      name: 'Active',
      value: metrics.activeSLAs,
    },
    {
      name: 'Completed',
      value: metrics.completedSLAs,
    },
    {
      name: 'Breached',
      value: metrics.breachedSLAs,
    },
  ];

  return (
    <div className="bg-white rounded-lg border border-gray-200 p-6">
      <h2 className="text-2xl font-semibold mb-6">SLA Metrics Dashboard</h2>

      <div className="grid grid-cols-4 gap-4 mb-8">
        <div className="bg-blue-50 rounded-lg p-4">
          <p className="text-gray-600 text-sm">Total SLAs</p>
          <p className="text-3xl font-bold">{metrics.totalSLAs}</p>
        </div>
        <div className="bg-green-50 rounded-lg p-4">
          <p className="text-gray-600 text-sm">Completed</p>
          <p className="text-3xl font-bold">{metrics.completedSLAs}</p>
        </div>
        <div className="bg-yellow-50 rounded-lg p-4">
          <p className="text-gray-600 text-sm">Active</p>
          <p className="text-3xl font-bold">{metrics.activeSLAs}</p>
        </div>
        <div className={`rounded-lg p-4 ${metrics.breachedSLAs > 0 ? 'bg-red-50' : 'bg-gray-50'}`}>
          <p className="text-gray-600 text-sm">Breached</p>
          <p className={`text-3xl font-bold ${metrics.breachedSLAs > 0 ? 'text-red-600' : ''}`}>
            {metrics.breachedSLAs}
          </p>
        </div>
      </div>

      <div className="grid grid-cols-2 gap-6 mb-6">
        <div className="bg-gray-50 rounded-lg p-4">
          <h3 className="font-semibold mb-4">SLA Distribution</h3>
          <ResponsiveContainer width="100%" height={300}>
            <BarChart data={breachChartData}>
              <CartesianGrid strokeDasharray="3 3" />
              <XAxis dataKey="name" />
              <YAxis />
              <Tooltip />
              <Bar dataKey="value" fill="#3b82f6" />
            </BarChart>
          </ResponsiveContainer>
        </div>

        <div className="bg-gray-50 rounded-lg p-4">
          <h3 className="font-semibold mb-4">Breach Statistics</h3>
          <div className="space-y-4">
            <div>
              <div className="flex justify-between mb-1">
                <span className="text-sm font-medium">Breach Rate</span>
                <span className="text-sm font-bold">{metrics.breachPercentage.toFixed(2)}%</span>
              </div>
              <div className="w-full bg-gray-200 rounded-full h-2">
                <div
                  className="bg-red-600 h-2 rounded-full"
                  style={{ width: `${Math.min(metrics.breachPercentage, 100)}%` }}
                ></div>
              </div>
            </div>

            <div className="bg-white rounded p-3">
              <p className="text-sm text-gray-600">Average Response Time</p>
              <p className="text-2xl font-bold">{metrics.averageResponseTime} min</p>
            </div>

            <div className="bg-white rounded p-3">
              <p className="text-sm text-gray-600">Average Resolution Time</p>
              <p className="text-2xl font-bold">{metrics.averageResolutionTime} min</p>
            </div>
          </div>
        </div>
      </div>

      {metrics.breachPercentage > 10 && (
        <div className="bg-red-50 border border-red-200 rounded-lg p-4 flex items-start gap-3">
          <AlertTriangle className="w-5 h-5 text-red-600 mt-0.5 flex-shrink-0" />
          <div>
            <h4 className="font-semibold text-red-800">High Breach Rate Alert</h4>
            <p className="text-sm text-red-700 mt-1">
              Your project has a {metrics.breachPercentage.toFixed(1)}% SLA breach rate. 
              Consider reviewing SLA policies or resource allocation.
            </p>
          </div>
        </div>
      )}
    </div>
  );
};

export default SLAMetricsDashboard;
