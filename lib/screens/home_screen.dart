import 'package:flutter/material.dart';
import 'package:url_launcher/url_launcher.dart';
import '../models/models.dart';

class HomeScreen extends StatefulWidget {
  final int tabIndex;
  const HomeScreen({super.key, required this.tabIndex});
  @override
  State<HomeScreen> createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> {
  String _searchQuery = '';
  String _selectedCarType = 'All';

  final List<ProviderModel> _dummyProviders = [
    ProviderModel(id: 'p1', name: 'Ramesh Mondal', phone: '+919830123456', profession: 'Car Driver (Sedan / SUV)', vehicle: 'Innova / Crysta', location: 'Dumdum, Kolkata', distanceKm: 0.8, rating: 4.9, reviewCount: 62, rates: CustomRateBox(visitingCharge: 100, hourlyRate: 120, dailyRate: 950, perKmRate: 14)),
    ProviderModel(id: 'p2', name: 'Bikramjit Ghosh', phone: '+919831198765', profession: 'JCB Driver & Heavy Fleet', vehicle: 'JCB (Earthmover)', location: 'Barasat, Kolkata', distanceKm: 2.4, rating: 4.8, reviewCount: 41, rates: CustomRateBox(visitingCharge: 500, hourlyRate: 850, dailyRate: 6500, perKmRate: 45)),
    ProviderModel(id: 'p3', name: 'Sheikh Nasiruddin', phone: '+919748211223', profession: 'AC Technician & HVAC Expert', vehicle: 'Bike / Scooter', location: 'Salt Lake Sector 5, Kolkata', distanceKm: 1.2, rating: 4.9, reviewCount: 98, rates: CustomRateBox(visitingCharge: 199, hourlyRate: 250, dailyRate: 1500, perKmRate: 10)),
    ProviderModel(id: 'p5', name: 'Amitava Roy', phone: '+919123456789', profession: 'Electrician & Wiring Pro', vehicle: 'Bike', location: 'Nagerbazar, Dumdum', distanceKm: 0.5, rating: 4.7, reviewCount: 53, rates: CustomRateBox(visitingCharge: 150, hourlyRate: 150, dailyRate: 1000, perKmRate: 10)),
  ];

  final List<String> _panIndiaSuggestions = [
    'AC Technician', 'HVAC Technician', 'Electrician', 'Plumber', 'Photographer', 'Beautician', 'Interior Decorator', 'Carpenter', 'Painter', 'Mechanic', 'Welder', 'Mason'
  ];

  @override
  Widget build(BuildContext context) {
    return _buildDriverOrTechnicianWall();
  }

  Widget _buildDriverOrTechnicianWall() {
    final filtered = _dummyProviders.where((p) {
      if (widget.tabIndex == 0 && !p.profession.toLowerCase().contains('driver')) return false;
      if (widget.tabIndex == 2 && p.profession.toLowerCase().contains('driver')) return false;
      if (_searchQuery.trim().isEmpty) return true;
      final q = _searchQuery.toLowerCase();
      return p.profession.toLowerCase().contains(q) || p.name.toLowerCase().contains(q);
    }).toList();

    return Column(children: [
      Padding(
        padding: const EdgeInsets.all(12),
        child: TextField(
          decoration: InputDecoration(
            hintText: widget.tabIndex == 0 ? "Search driver e.g. Sedan Driver..." : "Search e.g. AC Technician, Photographer, Beautician...",
            hintStyle: const TextStyle(fontSize: 12, color: Colors.black38),
            prefixIcon: const Icon(Icons.search),
            filled: true, fillColor: Colors.white,
            border: OutlineInputBorder(borderRadius: BorderRadius.circular(12), borderSide: BorderSide.none),
          ),
          onChanged: (val) => setState(() => _searchQuery = val),
        ),
      ),
      if (widget.tabIndex == 2)
        Container(
          margin: const EdgeInsets.symmetric(horizontal: 12),
          padding: const EdgeInsets.all(10),
          decoration: BoxDecoration(color: Colors.white, borderRadius: BorderRadius.circular(12), border: Border.all(color: Colors.black12)),
          child: Column(crossAxisAlignment: CrossAxisAlignment.start, children: [
            const Text('Popular Pan-India Trades:', style: TextStyle(fontSize: 10, fontWeight: FontWeight.bold, color: Colors.black45)),
            const SizedBox(height: 8),
            Wrap(spacing: 6, runSpacing: 6, children: _panIndiaSuggestions.map((s) => ActionChip(label: Text(s, style: const TextStyle(fontSize: 11)), backgroundColor: _searchQuery == s ? const Color(0xFFFFD700) : const Color(0xFFF5F5F5), onPressed: () => setState(() => _searchQuery = s))).toList()),
          ]),
        ),
      Expanded(child: ListView.builder(padding: const EdgeInsets.all(12), itemCount: filtered.length, itemBuilder: (ctx, i) => Card(child: ListTile(title: Text(filtered[i].name), subtitle: Text(filtered[i].profession), trailing: Text("${filtered[i].rating} ★"))))),
    ]);
  }
}
