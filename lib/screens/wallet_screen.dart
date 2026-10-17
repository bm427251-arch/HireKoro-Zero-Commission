import 'package:flutter/material.dart';
import 'package:url_launcher/url_launcher.dart';

class WalletScreen extends StatefulWidget {
  const WalletScreen({super.key});

  @override
  State<WalletScreen> createState() => _WalletScreenState();
}

class _WalletScreenState extends State<WalletScreen> {
  double _balance = 1250.0;
  final TextEditingController _amountController = TextEditingController(text: '500');

  void _addMoneyRazorpay() async {
    final amount = double.tryParse(_amountController.text) ?? 500;
    final paise = (amount * 100).toInt();
    final url = Uri.parse('https://razorpay.me/@bharatmitrainfotech?amount=$paise');
    if (await canLaunchUrl(url)) {
      await launchUrl(url, mode: LaunchMode.externalApplication);
      setState(() => _balance += amount);
      if (mounted) {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text('Loaded ₹${amount.toInt()} into Wallet via Razorpay!')),
        );
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.all(16),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          const Text('Wallet & Payouts', style: TextStyle(fontSize: 20, fontWeight: FontWeight.bold, color: Color(0xFF2B2B2B))),
          const Text('Zero commission platform funds & direct UPI settlements', style: TextStyle(fontSize: 12, color: Colors.black45)),
          const SizedBox(height: 16),
          Card(
            color: const Color(0xFF2B2B2B),
            shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(16)),
            child: Padding(
              padding: const EdgeInsets.all(20),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  const Text('AVAILABLE BALANCE', style: TextStyle(color: Colors.white60, fontSize: 11, fontWeight: FontWeight.bold)),
                  const SizedBox(height: 8),
                  Text('₹${_balance.toStringAsFixed(2)}', style: const TextStyle(fontSize: 32, fontWeight: FontWeight.w900, color: Color(0xFFFFD700))),
                  const SizedBox(height: 16),
                  Row(
                    children: [
                      Expanded(
                        child: ElevatedButton.icon(
                          icon: const Icon(Icons.add, size: 16),
                          label: const Text('Add Money'),
                          style: ElevatedButton.styleFrom(
                            backgroundColor: const Color(0xFFFFD700),
                            foregroundColor: Colors.black,
                            shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(8)),
                          ),
                          onPressed: () {
                            showDialog(
                              context: context,
                              builder: (ctx) => AlertDialog(
                                title: const Text('Add Money (Razorpay)'),
                                content: TextField(
                                  controller: _amountController,
                                  keyboardType: TextInputType.number,
                                  decoration: const InputDecoration(labelText: 'Amount (₹)', border: OutlineInputBorder()),
                                ),
                                actions: [
                                  TextButton(onPressed: () => Navigator.pop(ctx), child: const Text('Cancel')),
                                  ElevatedButton(
                                    style: ElevatedButton.styleFrom(backgroundColor: const Color(0xFFFFD700), foregroundColor: Colors.black),
                                    onPressed: () {
                                      Navigator.pop(ctx);
                                      _addMoneyRazorpay();
                                    },
                                    child: const Text('Pay via Razorpay'),
                                  ),
                                ],
                              ),
                            );
                          },
                        ),
                      ),
                      const SizedBox(width: 10),
                      Expanded(
                        child: OutlinedButton.icon(
                          icon: const Icon(Icons.arrow_outward, size: 16),
                          label: const Text('Withdraw'),
                          style: OutlinedButton.styleFrom(
                            foregroundColor: Colors.white,
                            side: const BorderSide(color: Colors.white),
                            shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(8)),
                          ),
                          onPressed: () {
                            ScaffoldMessenger.of(context).showSnackBar(
                              const SnackBar(content: Text('Withdrawal request submitted to your registered UPI ID!')),
                            );
                          },
                        ),
                      ),
                    ],
                  ),
                ],
              ),
            ),
          ),
          const SizedBox(height: 20),
          const Text('Recent Ledger Transactions', style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold)),
          const SizedBox(height: 10),
          Expanded(
            child: ListView(
              children: const [
                ListTile(
                  leading: CircleAvatar(backgroundColor: Color(0xFFE8F5E9), child: Icon(Icons.arrow_downward, color: Colors.green)),
                  title: Text('Referral Bonus (Friend Joined)', style: TextStyle(fontWeight: FontWeight.bold, fontSize: 14)),
                  subtitle: Text('15 Oct 2026 • HIRE50BM Code Claimed'),
                  trailing: Text('+₹50', style: TextStyle(color: Colors.green, fontWeight: FontWeight.bold, fontSize: 15)),
                ),
                ListTile(
                  leading: CircleAvatar(backgroundColor: Color(0xFFE8F5E9), child: Icon(Icons.arrow_downward, color: Colors.green)),
                  title: Text('Milestone 5 Referrals Bonus', style: TextStyle(fontWeight: FontWeight.bold, fontSize: 14)),
                  subtitle: Text('10 Oct 2026 • 5 Referrals Completed'),
                  trailing: Text('+₹500', style: TextStyle(color: Colors.green, fontWeight: FontWeight.bold, fontSize: 15)),
                ),
              ],
            ),
          ),
        ],
      ),
    );
  }
}
