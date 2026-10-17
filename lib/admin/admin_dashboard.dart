import 'package:flutter/material.dart';

class AdminDashboardScreen extends StatelessWidget {
  const AdminDashboardScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Admin Console (bm427251@gmail.com)'),
        backgroundColor: const Color(0xFF2B2B2B),
        foregroundColor: const Color(0xFFFFD700),
      ),
      body: ListView(
        padding: const EdgeInsets.all(16),
        children: [
          const Text('Overview Master Metrics', style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold, color: Color(0xFF2B2B2B))),
          const SizedBox(height: 12),
          Row(
            children: [
              _buildMetric('Active Users', '2,480', Colors.black87),
              const SizedBox(width: 10),
              _buildMetric('Providers', '384', const Color(0xFF2E7D32)),
            ],
          ),
          const SizedBox(height: 10),
          Row(
            children: [
              _buildMetric('Monthly GMV', '₹12.4 Lakh', const Color(0xFFE5C100)),
              const SizedBox(width: 10),
              _buildMetric('Sub Revenue', '₹2.9 Lakh', Colors.black87),
            ],
          ),
          const SizedBox(height: 20),
          const Text('A-Z Management & Governance', style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold)),
          const SizedBox(height: 10),
          ListTile(
            tileColor: Colors.white,
            shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(10)),
            leading: const Icon(Icons.work, color: Color(0xFF2B2B2B)),
            title: const Text('Manage A-Z Professions', style: TextStyle(fontWeight: FontWeight.bold)),
            subtitle: const Text('Add/Edit/Delete/Enable-Disable & assign slab'),
            trailing: const Icon(Icons.chevron_right),
            onTap: () {},
          ),
          const SizedBox(height: 8),
          ListTile(
            tileColor: Colors.white,
            shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(10)),
            leading: const Icon(Icons.directions_car, color: Color(0xFF2B2B2B)),
            title: const Text('Manage A-Z Vehicles', style: TextStyle(fontWeight: FontWeight.bold)),
            subtitle: const Text('Toto, Auto, JCB, Ambulance, Trucks & Luxury fleet'),
            trailing: const Icon(Icons.chevron_right),
            onTap: () {},
          ),
          const SizedBox(height: 8),
          ListTile(
            tileColor: Colors.white,
            shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(10)),
            leading: const Icon(Icons.warning, color: Colors.red),
            title: const Text('Complaints & Quality Audit', style: TextStyle(fontWeight: FontWeight.bold)),
            subtitle: const Text('Warning, Deduct Wallet Penalty, Block (<3.5 Auto Review)'),
            trailing: const Icon(Icons.chevron_right),
            onTap: () {},
          ),
        ],
      ),
    );
  }

  Widget _buildMetric(String label, String value, Color color) {
    return Expanded(
      child: Card(
        shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
        child: Padding(
          padding: const EdgeInsets.all(14),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Text(label, style: const TextStyle(fontSize: 11, color: Colors.black45, fontWeight: FontWeight.bold)),
              const SizedBox(height: 4),
              Text(value, style: TextStyle(fontSize: 20, fontWeight: FontWeight.w900, color: color)),
            ],
          ),
        ),
      ),
    );
  }
}
