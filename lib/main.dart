import 'package:flutter/material.dart';
import 'package:shared_preferences/shared_preferences.dart';

class AdminDashboardScreen extends StatefulWidget {
  const AdminDashboardScreen({super.key});

  @override
  State<AdminDashboardScreen> createState() => _AdminDashboardScreenState();
}

class _AdminDashboardScreenState extends State<AdminDashboardScreen> {
  String currentEmail = '';
  String currentPass = '';

  @override
  void initState() {
    super.initState();
    _loadCurrentCreds();
  }

  Future<void> _loadCurrentCreds() async {
    final prefs = await SharedPreferences.getInstance();
    setState(() {
      currentEmail = prefs.getString('admin_email') ?? 'bm427251@gmail.com';
      currentPass = prefs.getString('admin_password') ?? 'HireKoro@2025Bm';
    });
  }

  void _showChangeCredentialsDialog() {
    final emailCtrl = TextEditingController(text: currentEmail);
    final passCtrl = TextEditingController(text: currentPass);
    bool obscure = true;

    showDialog(
      context: context,
      builder: (ctx) => StatefulBuilder(
        builder: (context, setDialogState) => AlertDialog(
          title: const Text('Change Admin Login', style: TextStyle(fontWeight: FontWeight.bold)),
          content: Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              TextField(
                controller: emailCtrl,
                decoration: const InputDecoration(labelText: 'New Admin Email', border: OutlineInputBorder(), prefixIcon: Icon(Icons.email)),
              ),
              const SizedBox(height: 12),
              TextField(
                controller: passCtrl,
                obscureText: obscure,
                decoration: InputDecoration(
                  labelText: 'New Password',
                  border: const OutlineInputBorder(),
                  prefixIcon: const Icon(Icons.lock),
                  suffixIcon: IconButton(
                    icon: Icon(obscure ? Icons.visibility_off : Icons.visibility),
                    onPressed: () => setDialogState(() => obscure = !obscure),
                  ),
                ),
              ),
            ],
          ),
          actions: [
            TextButton(onPressed: () => Navigator.pop(ctx), child: const Text('Cancel')),
            ElevatedButton(
              style: ElevatedButton.styleFrom(backgroundColor: const Color(0xFFFFD700), foregroundColor: Colors.black),
              onPressed: () async {
                final newEmail = emailCtrl.text.trim();
                final newPass = passCtrl.text.trim();
                if (newEmail.isEmpty || newPass.isEmpty) return;

                final prefs = await SharedPreferences.getInstance();
                await prefs.setString('admin_email', newEmail);
                await prefs.setString('admin_password', newPass);

                setState(() {
                  currentEmail = newEmail;
                  currentPass = newPass;
                });

                Navigator.pop(ctx);
                ScaffoldMessenger.of(context).showSnackBar(SnackBar(content: Text('Admin Login Updated!\nEmail: $newEmail\nPass: $newPass')));
              },
              child: const Text('SAVE NEW LOGIN', style: TextStyle(fontWeight: FontWeight.bold)),
            ),
          ],
        ),
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Admin Console ($currentEmail)'),
        backgroundColor: const Color(0xFF2B2B2B),
        foregroundColor: const Color(0xFFFFD700),
        actions: [
          IconButton(icon: const Icon(Icons.settings), onPressed: _showChangeCredentialsDialog),
        ],
      ),
      body: ListView(
        padding: const EdgeInsets.all(16),
        children: [
          Card(
            color: const Color(0xFFFFD700),
            shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
            child: ListTile(
              leading: const Icon(Icons.security, color: Colors.black),
              title: const Text('Admin Security Settings', style: TextStyle(fontWeight: FontWeight.bold)),
              subtitle: Text('Email: $currentEmail\nTap to change Email & Password'),
              trailing: const Icon(Icons.edit, color: Colors.black),
              onTap: _showChangeCredentialsDialog,
            ),
          ),
          const SizedBox(height: 20),
          const Text('Overview Master Metrics', style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold, color: Color(0xFF2B2B2B))),
          const SizedBox(height: 12),
          Row(children: [_buildMetric('Active Users', '2,480', Colors.black87), const SizedBox(width: 10), _buildMetric('Providers', '384', const Color(0xFF2E7D32))]),
          const SizedBox(height: 10),
          Row(children: [_buildMetric('Monthly GMV', '₹12.4 Lakh', const Color(0xFFE5C100)), const SizedBox(width: 10), _buildMetric('Sub Revenue', '₹2.9 Lakh', Colors.black87)]),
          const SizedBox(height: 20),
          const Text('A-Z Management & Governance', style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold)),
          const SizedBox(height: 10),
          ListTile(tileColor: Colors.white, shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(10)), leading: const Icon(Icons.work, color: Color(0xFF2B2B2B)), title: const Text('Manage A-Z Professions', style: TextStyle(fontWeight: FontWeight.bold)), subtitle: const Text('Add/Edit/Delete/Enable-Disable & assign slab'), trailing: const Icon(Icons.chevron_right), onTap: () {}),
          const SizedBox(height: 8),
          ListTile(tileColor: Colors.white, shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(10)), leading: const Icon(Icons.directions_car, color: Color(0xFF2B2B2B)), title: const Text('Manage A-Z Vehicles', style: TextStyle(fontWeight: FontWeight.bold)), subtitle: const Text('Toto, Auto, JCB, Ambulance, Trucks & Luxury fleet'), trailing: const Icon(Icons.chevron_right), onTap: () {}),
          const SizedBox(height: 8),
          ListTile(tileColor: Colors.white, shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(10)), leading: const Icon(Icons.warning, color: Colors.red), title: const Text('Complaints & Quality Audit', style: TextStyle(fontWeight: FontWeight.bold)), subtitle: const Text('Warning, Deduct Wallet Penalty, Block (<3.5 Auto Review)'), trailing: const Icon(Icons.chevron_right), onTap: () {}),
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
          child: Column(crossAxisAlignment: CrossAxisAlignment.start, children: [
            Text(label, style: const TextStyle(fontSize: 11, color: Colors.black45, fontWeight: FontWeight.bold)),
            const SizedBox(height: 4),
            Text(value, style: TextStyle(fontSize: 20, fontWeight: FontWeight.w900, color: color)),
          ]),
        ),
      ),
    );
  }
}
