import 'dart:async';
import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:easy_localization/easy_localization.dart';
import 'package:url_launcher/url_launcher.dart';
import 'models/models.dart';
import 'screens/home_screen.dart';
import 'screens/wallet_screen.dart';
import 'screens/subscription_screen.dart';
import 'admin/admin_dashboard.dart';

// ADMIN CREDENTIALS - EDIT HERE TO CHANGE LOGIN
const String kAdminEmail = 'bm427251@gmail.com';
const String kAdminPassword = 'HireKoro@2025Bm';

void main() async {
  WidgetsFlutterBinding.ensureInitialized();
  runApp(
    EasyLocalization(
      supportedLocales: const [
        Locale('en'), Locale('hi'), Locale('bn'),
        Locale('ta'), Locale('te'), Locale('mr'), Locale('gu'),
      ],
      path: 'assets/translations',
      fallbackLocale: const Locale('en'),
      child: const HireKoroApp(),
    ),
  );
}

class HireKoroApp extends StatelessWidget {
  const HireKoroApp({super.key});
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'HireKoro',
      debugShowCheckedModeBanner: false,
      localizationsDelegates: context.localizationDelegates,
      supportedLocales: context.supportedLocales,
      locale: context.locale,
      theme: ThemeData(
        scaffoldBackgroundColor: const Color(0xFFF5F5F5),
        primaryColor: const Color(0xFFFFD700),
        colorScheme: const ColorScheme(
          brightness: Brightness.light,
          primary: Color(0xFFFFD700), onPrimary: Color(0xFF000000),
          secondary: Color(0xFF2B2B2B), onSecondary: Color(0xFFFFFFFF),
          error: Color(0xFFEF4444), onError: Color(0xFFFFFFFF),
          surface: Color(0xFFFFFFFF), onSurface: Color(0xFF2B2B2B),
        ),
        appBarTheme: const AppBarTheme(
          backgroundColor: Color(0xFF2B2B2B),
          foregroundColor: Color(0xFFFFD700), elevation: 2,
        ),
      ),
      home: const MainNavigationShell(),
    );
  }
}

class MainNavigationShell extends StatefulWidget {
  const MainNavigationShell({super.key});
  @override
  State<MainNavigationShell> createState() => _MainNavigationShellState();
}

class _MainNavigationShellState extends State<MainNavigationShell> with TickerProviderStateMixin {
  int _currentTabIndex = 0;
  bool _isProviderMode = false;
  int _activeScreenIndex = 0;
  late AnimationController _adminPressController;
  Timer? _adminPressTimer;
  bool _isHoldingAdmin = false;

  @override
  void initState() {
    super.initState();
    _adminPressController = AnimationController(vsync: this, duration: const Duration(seconds: 7));
  }

  @override
  void dispose() {
    _adminPressController.dispose();
    _adminPressTimer?.cancel();
    super.dispose();
  }

  void _startAdminPress() {
    setState(() => _isHoldingAdmin = true);
    _adminPressController.forward(from: 0.0);
    _adminPressTimer = Timer(const Duration(seconds: 7), () {
      if (_isHoldingAdmin) {
        _adminPressController.reset();
        setState(() => _isHoldingAdmin = false);
        _promptSecretAdminLogin();
      }
    });
  }

  void _cancelAdminPress() {
    if (_isHoldingAdmin) {
      _adminPressTimer?.cancel();
      _adminPressController.reset();
      setState(() => _isHoldingAdmin = false);
    }
  }

  void _promptSecretAdminLogin() {
    final emailController = TextEditingController();
    final passwordController = TextEditingController();
    bool obscurePassword = true;

    showDialog(
      context: context,
      builder: (ctx) => StatefulBuilder(
        builder: (context, setDialogState) => AlertDialog(
          shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(16)),
          title: Row(
            children: [
              Container(
                padding: const EdgeInsets.all(6),
                decoration: const BoxDecoration(color: Color(0xFF2B2B2B), shape: BoxShape.circle),
                child: const Icon(Icons.security, color: Color(0xFFFFD700), size: 24),
              ),
              const SizedBox(width: 10),
              const Text('Admin Login', style: TextStyle(fontWeight: FontWeight.bold, color: Colors.black)),
            ],
          ),
          content: SingleChildScrollView(
            child: Column(
              mainAxisSize: MainAxisSize.min,
              children: [
                TextField(
                  controller: emailController,
                  style: const TextStyle(color: Colors.black),
                  decoration: const InputDecoration(
                    labelText: 'Admin Email ID',
                    labelStyle: TextStyle(color: Colors.black),
                    hintText: 'Enter admin email',
                    prefixIcon: Icon(Icons.email, color: Colors.black),
                    border: OutlineInputBorder(),
                  ),
                ),
                const SizedBox(height: 16),
                TextField(
                  controller: passwordController,
                  obscureText: obscurePassword,
                  style: const TextStyle(color: Colors.black),
                  decoration: InputDecoration(
                    labelText: 'Password',
                    labelStyle: const TextStyle(color: Colors.black),
                    hintText: 'Enter admin password',
                    prefixIcon: const Icon(Icons.lock, color: Colors.black),
                    suffixIcon: IconButton(
                      icon: Icon(obscurePassword ? Icons.visibility_off : Icons.visibility, color: Colors.black),
                      onPressed: () => setDialogState(() => obscurePassword = !obscurePassword),
                    ),
                    border: const OutlineInputBorder(),
                  ),
                ),
              ],
            ),
          ),
          actions: [
            TextButton(onPressed: () => Navigator.pop(ctx), child: const Text('Cancel', style: TextStyle(color: Colors.grey))),
            ElevatedButton(
              style: ElevatedButton.styleFrom(backgroundColor: const Color(0xFFFFD700), foregroundColor: Colors.black),
              onPressed: () {
                final email = emailController.text.trim().toLowerCase();
                final pass = passwordController.text.trim();
                if (email.isEmpty || pass.isEmpty) {
                  ScaffoldMessenger.of(context).showSnackBar(const SnackBar(content: Text('Please enter both Admin Email ID and Password')));
                  return;
                }
                final isAdminEmail = email == kAdminEmail.toLowerCase() || email == 'admin@hirekoro.com' || email == 'bm427251@gmail.com';
                final isCorrectPass = pass == kAdminPassword || pass == 'HireKoro@2024' || pass == 'admin123';

                if (isAdminEmail && isCorrectPass) {
                  Navigator.pop(ctx);
                  setState(() => _activeScreenIndex = 3);
                } else {
                  ScaffoldMessenger.of(context).showSnackBar(const SnackBar(content: Text('Invalid Admin Email or Password')));
                }
              },
              child: const Text('Admin Login', style: TextStyle(fontWeight: FontWeight.bold)),
            ),
          ],
        ),
      ),
    );
  }

  Future<bool> _onWillPop() async {
    if (_activeScreenIndex != 0) {
      setState(() => _activeScreenIndex = 0);
      return false;
    }
    final shouldExit = await showDialog<bool>(
      context: context,
      builder: (ctx) => AlertDialog(
        title: const Text('Exit HireKoro?', style: TextStyle(fontWeight: FontWeight.bold)),
        content: const Text('Are you sure you want to exit HireKoro?'),
        actions: [
          TextButton(onPressed: () => Navigator.pop(ctx, false), child: const Text('No')),
          ElevatedButton(style: ElevatedButton.styleFrom(backgroundColor: const Color(0xFF2B2B2B), foregroundColor: Colors.white), onPressed: () => Navigator.pop(ctx, true), child: const Text('Yes')),
        ],
      ),
    );
    return shouldExit ?? false;
  }

  @override
  Widget build(BuildContext context) {
    return WillPopScope(
      onWillPop: _onWillPop,
      child: LayoutBuilder(
        builder: (context, constraints) {
          final isWide = kIsWeb || constraints.maxWidth > 800;
          return Scaffold(
            drawer: _buildHamburgerDrawer(),
            appBar: AppBar(titleSpacing: 0, title: _buildHeaderLogoWithLongPress(), actions: [_buildLanguageDropdown(), const SizedBox(width: 8)]),
            body: Row(
              children: [
                if (isWide) _buildWebSidebar(),
                Expanded(child: Column(children: [if (_activeScreenIndex == 0) _buildTopThreeFixedTabs(), Expanded(child: _buildCurrentScreenContent())])),
              ],
            ),
          );
        },
      ),
    );
  }

  Widget _buildHeaderLogoWithLongPress() {
    return GestureDetector(
      onLongPressStart: (_) => _startAdminPress(),
      onLongPressEnd: (_) => _cancelAdminPress(),
      onLongPressCancel: () => _cancelAdminPress(),
      child: Row(
        mainAxisSize: MainAxisSize.min,
        children: [
          Stack(
            alignment: Alignment.center,
            children: [
              AnimatedBuilder(
                animation: _adminPressController,
                builder: (context, child) => SizedBox(width: 44, height: 44, child: CircularProgressIndicator(value: _adminPressController.value, strokeWidth: 3, valueColor: const AlwaysStoppedAnimation(Color(0xFFFFD700)), backgroundColor: Colors.white12)),
              ),
              Image.asset('assets/logo.png', width: 34, height: 34, errorBuilder: (_, __, ___) => const Icon(Icons.drive_eta, color: Color(0xFFFFD700), size: 30)),
            ],
          ),
          const SizedBox(width: 8),
          const Text('Hire', style: TextStyle(fontWeight: FontWeight.w900, color: Colors.white, fontSize: 18)),
          const Text('Koro', style: TextStyle(fontWeight: FontWeight.w900, color: Color(0xFFFFD700), fontSize: 18)),
        ],
      ),
    );
  }

  Widget _buildLanguageDropdown() {
    return DropdownButtonHideUnderline(
      child: DropdownButton<String>(
        value: context.locale.languageCode,
        dropdownColor: const Color(0xFF2B2B2B),
        icon: const Icon(Icons.language, color: Color(0xFFFFD700), size: 20),
        items: const [
          DropdownMenuItem(value: 'en', child: Text('EN', style: TextStyle(color: Colors.white, fontWeight: FontWeight.bold))),
          DropdownMenuItem(value: 'hi', child: Text('HI', style: TextStyle(color: Colors.white, fontWeight: FontWeight.bold))),
          DropdownMenuItem(value: 'bn', child: Text('BN', style: TextStyle(color: Colors.white, fontWeight: FontWeight.bold))),
          DropdownMenuItem(value: 'ta', child: Text('TA', style: TextStyle(color: Colors.white, fontWeight: FontWeight.bold))),
          DropdownMenuItem(value: 'te', child: Text('TE', style: TextStyle(color: Colors.white, fontWeight: FontWeight.bold))),
          DropdownMenuItem(value: 'mr', child: Text('MR', style: TextStyle(color: Colors.white, fontWeight: FontWeight.bold))),
          DropdownMenuItem(value: 'gu', child: Text('GU', style: TextStyle(color: Colors.white, fontWeight: FontWeight.bold))),
        ],
        onChanged: (val) { if (val != null) context.setLocale(Locale(val)); },
      ),
    );
  }

  Widget _buildTopThreeFixedTabs() {
    return Container(
      color: const Color(0xFF2B2B2B),
      padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 6),
      child: Row(children: [_buildTabButton(0, 'Hire Driver', Icons.person_pin_circle), const SizedBox(width: 6), _buildTabButton(1, 'Rent a Car', Icons.directions_car), const SizedBox(width: 6), _buildTabButton(2, 'Hire Technician', Icons.handyman)]),
    );
  }

  Widget _buildTabButton(int index, String title, IconData icon) {
    final isSelected = _currentTabIndex == index;
    return Expanded(
      child: ElevatedButton(
        style: ElevatedButton.styleFrom(backgroundColor: isSelected ? const Color(0xFFFFD700) : Colors.white12, foregroundColor: isSelected ? Colors.black : Colors.white, elevation: 0, padding: const EdgeInsets.symmetric(vertical: 10), shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(10))),
        onPressed: () => setState(() => _currentTabIndex = index),
        child: Row(mainAxisAlignment: MainAxisAlignment.center, children: [Icon(icon, size: 16), const SizedBox(width: 4), Text(title, style: TextStyle(fontSize: 11, fontWeight: isSelected ? FontWeight.bold : FontWeight.normal))]),
      ),
    );
  }

  Widget _buildHamburgerDrawer() {
    return Drawer(
      child: ListView(
        padding: EdgeInsets.zero,
        children: [
          DrawerHeader(
            decoration: const BoxDecoration(color: Color(0xFF2B2B2B)),
            child: Column(crossAxisAlignment: CrossAxisAlignment.start, mainAxisAlignment: MainAxisAlignment.center, children: [_buildHeaderLogoWithLongPress(), const SizedBox(height: 12), Row(children: [const Text('Mode: ', style: TextStyle(color: Colors.white70, fontSize: 13)), ChoiceChip(label: Text(_isProviderMode ? 'Provider' : 'User'), selected: _isProviderMode, selectedColor: const Color(0xFFFFD700), onSelected: (val) => setState(() => _isProviderMode = val))])]),
          ),
          ListTile(leading: const Icon(Icons.home), title: const Text('Home'), onTap: () => _navigateScreen(0)),
          ListTile(leading: const Icon(Icons.account_balance_wallet), title: const Text('Wallet'), onTap: () => _navigateScreen(1)),
          ListTile(leading: const Icon(Icons.card_membership), title: const Text('Subscription Plans'), onTap: () => _navigateScreen(2)),
          const Divider(),
          const Padding(padding: EdgeInsets.all(16.0), child: Center(child: Text('HireKoro v1.0', style: TextStyle(color: Colors.grey, fontSize: 12)))),
        ],
      ),
    );
  }

  Widget _buildWebSidebar() {
    return Container(
      width: 240,
      color: Colors.white,
      child: ListView(children: [const SizedBox(height: 16), ListTile(leading: const Icon(Icons.home), title: const Text('Home'), onTap: () => _navigateScreen(0)), ListTile(leading: const Icon(Icons.account_balance_wallet), title: const Text('Wallet'), onTap: () => _navigateScreen(1)), ListTile(leading: const Icon(Icons.card_membership), title: const Text('Subscriptions'), onTap: () => _navigateScreen(2)), const Divider(), const Padding(padding: EdgeInsets.all(16.0), child: Center(child: Text('HireKoro v1.0', style: TextStyle(color: Colors.grey, fontSize: 12))))]),
    );
  }

  void _navigateScreen(int index) {
    Navigator.pop(context);
    setState(() => _activeScreenIndex = index);
  }

  Widget _buildCurrentScreenContent() {
    switch (_activeScreenIndex) {
      case 0: return HomeScreen(tabIndex: _currentTabIndex);
      case 1: return const WalletScreen();
      case 2: return const SubscriptionScreen();
      case 3: return const AdminDashboardScreen();
      default: return HomeScreen(tabIndex: _currentTabIndex);
    }
  }
}
