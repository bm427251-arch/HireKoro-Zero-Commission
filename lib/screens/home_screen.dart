import 'package:flutter/material.dart';
import 'package:url_launcher/url_launcher.dart';
import '../models/models.dart';

class HomeScreen extends StatefulWidget {
  final int tabIndex; // 0: Driver, 1: Car, 2: Technician
  const HomeScreen({super.key, required this.tabIndex});

  @override
  State<HomeScreen> createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> {
  String _searchQuery = '';
  String _selectedCarType = 'All';
  String _selectedFuel = 'All';
  String _selectedDriveMode = 'All';

  // Driver / Technician Data
  final List<ProviderModel> _dummyProviders = [
    ProviderModel(
      id: 'p1',
      name: 'Ramesh Mondal',
      phone: '+919830123456',
      profession: 'Car Driver (Sedan / SUV)',
      vehicle: 'Innova / Crysta',
      location: 'Dumdum, Kolkata',
      distanceKm: 0.8,
      rating: 4.9,
      reviewCount: 62,
      rates: CustomRateBox(visitingCharge: 100, hourlyRate: 120, dailyRate: 950, perKmRate: 14),
    ),
    ProviderModel(
      id: 'p2',
      name: 'Bikramjit Ghosh',
      phone: '+919831198765',
      profession: 'JCB Driver & Heavy Fleet',
      vehicle: 'JCB (Earthmover)',
      location: 'Barasat, Kolkata',
      distanceKm: 2.4,
      rating: 4.8,
      reviewCount: 41,
      rates: CustomRateBox(visitingCharge: 500, hourlyRate: 850, dailyRate: 6500, perKmRate: 45),
    ),
    ProviderModel(
      id: 'p3',
      name: 'Sheikh Nasiruddin',
      phone: '+919748211223',
      profession: 'AC Mistri & Cooling Expert',
      vehicle: 'Bike / Scooter',
      location: 'Salt Lake Sector 5, Kolkata',
      distanceKm: 1.2,
      rating: 4.9,
      reviewCount: 98,
      rates: CustomRateBox(visitingCharge: 199, hourlyRate: 250, dailyRate: 1500, perKmRate: 10),
    ),
    ProviderModel(
      id: 'p4',
      name: 'Subhasish Das',
      phone: '+919007154321',
      profession: 'Toto / Auto City Driver',
      vehicle: 'Toto (E-Rickshaw)',
      location: 'Dumdum Cantonment',
      distanceKm: 0.3,
      rating: 4.6,
      reviewCount: 28,
      rates: CustomRateBox(visitingCharge: 50, hourlyRate: 100, dailyRate: 600, perKmRate: 12),
    ),
    ProviderModel(
      id: 'p5',
      name: 'Amitava Roy',
      phone: '+919123456789',
      profession: 'Electrician & Wiring Pro',
      vehicle: 'Bike',
      location: 'Nagerbazar, Dumdum',
      distanceKm: 0.5,
      rating: 4.7,
      reviewCount: 53,
      rates: CustomRateBox(visitingCharge: 150, hourlyRate: 150, dailyRate: 1000, perKmRate: 10),
    ),
  ];

  // Dedicated Rent a Car Collection ("rent_cars")
  final List<RentCarModel> _dummyRentCars = [
    RentCarModel(
      id: 'rc_1',
      carName: 'Maruti Swift Dzire',
      carModel: 'VXi 2022 Sedan',
      carType: 'Sedan',
      fuel: 'Petrol',
      seats: '5 Seater',
      transmission: 'Manual',
      ownerType: 'Individual',
      ownerName: 'Amit Roy',
      ownerPhone: '+919830122334',
      pricePerHour: 120,
      pricePerDay: 1400,
      pricePerKm: 12,
      withDriverAvailable: true,
      selfDriveAvailable: true,
      isAvailableNow: true,
      location: 'Salt Lake, Kolkata',
      distanceKm: 1.2,
      rating: 4.8,
      reviewCount: 32,
    ),
    RentCarModel(
      id: 'rc_2',
      carName: 'Toyota Innova Crysta',
      carModel: 'ZX 2023 7-Seater Luxury',
      carType: 'SUV',
      fuel: 'Diesel',
      seats: '7 Seater',
      transmission: 'Automatic',
      ownerType: 'Agency',
      ownerName: 'Kolkata City Fleet',
      ownerPhone: '+919831144556',
      pricePerHour: 280,
      pricePerDay: 3200,
      pricePerKm: 18,
      withDriverAvailable: true,
      selfDriveAvailable: true,
      isAvailableNow: true,
      location: 'Dumdum Airport, Kolkata',
      distanceKm: 0.9,
      rating: 4.9,
      reviewCount: 78,
    ),
    RentCarModel(
      id: 'rc_3',
      carName: 'Hyundai Creta',
      carModel: 'SX Turbo 2023 Panoramic',
      carType: 'SUV',
      fuel: 'Petrol',
      seats: '5 Seater',
      transmission: 'Automatic',
      ownerType: 'Individual',
      ownerName: 'Rahul Sen',
      ownerPhone: '+919748266778',
      pricePerHour: 220,
      pricePerDay: 2600,
      pricePerKm: 15,
      withDriverAvailable: true,
      selfDriveAvailable: true,
      isAvailableNow: true,
      location: 'New Town, Action Area 1',
      distanceKm: 2.1,
      rating: 4.9,
      reviewCount: 45,
    ),
    RentCarModel(
      id: 'rc_4',
      carName: 'Maruti Suzuki WagonR',
      carModel: 'LXi 1.2L 2023 Eco',
      carType: 'Hatchback',
      fuel: 'CNG',
      seats: '5 Seater',
      transmission: 'Manual',
      ownerType: 'Agency',
      ownerName: 'Metro Rent-a-Car',
      ownerPhone: '+919123488990',
      pricePerHour: 80,
      pricePerDay: 999,
      pricePerKm: 9,
      withDriverAvailable: true,
      selfDriveAvailable: true,
      isAvailableNow: true,
      location: 'Nagerbazar, Dumdum',
      distanceKm: 0.6,
      rating: 4.7,
      reviewCount: 29,
    ),
    RentCarModel(
      id: 'rc_5',
      carName: 'BMW 3 Series Gran Limousine',
      carModel: '330Li Luxury Line 2022',
      carType: 'Luxury',
      fuel: 'Petrol',
      seats: '5 Seater',
      transmission: 'Automatic',
      ownerType: 'Agency',
      ownerName: 'Royal Bengal Prestige Motors',
      ownerPhone: '+919883055667',
      pricePerHour: 650,
      pricePerDay: 7500,
      pricePerKm: 40,
      withDriverAvailable: true,
      selfDriveAvailable: true,
      isAvailableNow: true,
      location: 'Park Street, Kolkata',
      distanceKm: 3.5,
      rating: 5.0,
      reviewCount: 19,
    ),
    RentCarModel(
      id: 'rc_6',
      carName: 'Mahindra Thar 4x4',
      carModel: 'LX Hard Top Diesel 2023',
      carType: 'SUV',
      fuel: 'Diesel',
      seats: '4 Seater',
      transmission: 'Manual',
      ownerType: 'Individual',
      ownerName: 'Bikash Adventure Rides',
      ownerPhone: '+919007133445',
      pricePerHour: 260,
      pricePerDay: 3000,
      pricePerKm: 16,
      withDriverAvailable: true,
      selfDriveAvailable: true,
      isAvailableNow: true,
      location: 'Rajarhat Expressway',
      distanceKm: 1.8,
      rating: 4.8,
      reviewCount: 56,
    ),
  ];

  @override
  Widget build(BuildContext context) {
    if (widget.tabIndex == 1) {
      return _buildRentACarWall();
    } else {
      return _buildDriverOrTechnicianWall();
    }
  }

  // =================== RENT A CAR WALL ===================
  Widget _buildRentACarWall() {
    final filteredCars = _dummyRentCars.where((c) {
      if (_selectedCarType != 'All' && c.carType != _selectedCarType) return false;
      if (_selectedFuel != 'All' && c.fuel != _selectedFuel) return false;
      if (_selectedDriveMode == 'With Driver' && !c.withDriverAvailable) return false;
      if (_selectedDriveMode == 'Self Drive' && !c.selfDriveAvailable) return false;

      if (_searchQuery.trim().isEmpty) return true;
      final q = _searchQuery.toLowerCase();
      final words = q.split(' ').where((w) => w.isNotEmpty);
      return words.every((w) =>
          c.carName.toLowerCase().contains(w) ||
          c.carModel.toLowerCase().contains(w) ||
          c.carType.toLowerCase().contains(w) ||
          c.location.toLowerCase().contains(w));
    }).toList();

    return Column(
      children: [
        // Dedicated Car Search
        Padding(
          padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 8),
          child: TextField(
            decoration: InputDecoration(
              hintText: "Search car name, e.g. 'Innova', 'Swift', 'Thar'...",
              prefixIcon: const Icon(Icons.directions_car, color: Color(0xFF2B2B2B)),
              filled: true,
              fillColor: Colors.white,
              contentPadding: const EdgeInsets.symmetric(horizontal: 16),
              border: OutlineInputBorder(borderRadius: BorderRadius.circular(12), borderSide: BorderSide.none),
            ),
            onChanged: (val) => setState(() => _searchQuery = val),
          ),
        ),

        // Rent a Car Only Filters: Hatchback, Sedan, SUV, Luxury
        SingleChildScrollView(
          scrollDirection: Axis.horizontal,
          padding: const EdgeInsets.symmetric(horizontal: 12),
          child: Row(
            children: ['All', 'Hatchback', 'Sedan', 'SUV', 'Luxury'].map((type) {
              final isSel = _selectedCarType == type;
              return Padding(
                padding: const EdgeInsets.only(right: 6),
                child: FilterChip(
                  label: Text(type, style: TextStyle(fontSize: 11, fontWeight: isSel ? FontWeight.bold : FontWeight.normal)),
                  selected: isSel,
                  selectedColor: const Color(0xFFFFD700),
                  backgroundColor: Colors.white,
                  onSelected: (val) => setState(() => _selectedCarType = type),
                ),
              );
            }).toList(),
          ),
        ),

        const SizedBox(height: 6),

        // 16:9 Large Car Listings
        Expanded(
          child: ListView.separated(
            padding: const EdgeInsets.all(12),
            itemCount: filteredCars.length,
            separatorBuilder: (_, __) => const SizedBox(height: 14),
            itemBuilder: (context, index) {
              final car = filteredCars[index];
              return _buildLarge16x9CarCard(car);
            },
          ),
        ),
      ],
    );
  }

  Widget _buildLarge16x9CarCard(RentCarModel car) {
    return Card(
      elevation: 3,
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(16)),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          // 16:9 Aspect Ratio Hero Banner
          AspectRatio(
            aspectRatio: 16 / 9,
            child: Container(
              decoration: const BoxDecoration(
                color: Color(0xFF2B2B2B),
                borderRadius: BorderRadius.vertical(top: Radius.circular(16)),
              ),
              padding: const EdgeInsets.all(12),
              child: Stack(
                children: [
                  // Badges Top
                  Row(
                    mainAxisAlignment: MainAxisAlignment.spaceBetween,
                    children: [
                      Row(
                        children: [
                          Container(
                            padding: const EdgeInsets.symmetric(horizontal: 6, vertical: 3),
                            decoration: BoxDecoration(color: Colors.green, borderRadius: BorderRadius.circular(4)),
                            child: const Text('Available Now', style: TextStyle(color: Colors.white, fontSize: 10, fontWeight: FontWeight.bold)),
                          ),
                          const SizedBox(width: 6),
                          Container(
                            padding: const EdgeInsets.symmetric(horizontal: 6, vertical: 3),
                            decoration: BoxDecoration(color: const Color(0xFFFFD700), borderRadius: BorderRadius.circular(4)),
                            child: Text(car.carType.toUpperCase(), style: const TextStyle(color: Colors.black, fontSize: 10, fontWeight: FontWeight.bold)),
                          ),
                        ],
                      ),
                      Container(
                        padding: const EdgeInsets.symmetric(horizontal: 6, vertical: 3),
                        decoration: BoxDecoration(color: Colors.black54, borderRadius: BorderRadius.circular(4)),
                        child: Text('${car.rating} ★ (${car.reviewCount})', style: const TextStyle(color: Colors.white, fontSize: 10, fontWeight: FontWeight.bold)),
                      ),
                    ],
                  ),
                  // Center Car Graphic
                  Center(
                    child: Column(
                      mainAxisSize: MainAxisSize.min,
                      children: [
                        const Icon(Icons.directions_car, size: 56, color: Color(0xFFFFD700)),
                        Text(car.carModel, style: const TextStyle(color: Colors.white70, fontSize: 11)),
                      ],
                    ),
                  ),
                  // Bottom options
                  Positioned(
                    bottom: 0,
                    left: 0,
                    child: Row(
                      children: [
                        if (car.selfDriveAvailable)
                          const Text('✓ Self Drive  ', style: TextStyle(color: Colors.white, fontSize: 10, fontWeight: FontWeight.bold)),
                        if (car.withDriverAvailable)
                          const Text('✓ With Driver', style: TextStyle(color: Colors.white, fontSize: 10, fontWeight: FontWeight.bold)),
                      ],
                    ),
                  ),
                ],
              ),
            ),
          ),

          // Details Row
          Padding(
            padding: const EdgeInsets.all(14),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Text(car.carName, style: const TextStyle(fontSize: 16, fontWeight: FontWeight.bold)),
                Text('Owner: ${car.ownerName} (${car.ownerType})', style: const TextStyle(fontSize: 11, color: Colors.black54)),
                const SizedBox(height: 6),
                Row(
                  children: [
                    _buildSpecChip(car.fuel),
                    const SizedBox(width: 6),
                    _buildSpecChip(car.transmission),
                    const SizedBox(width: 6),
                    _buildSpecChip(car.seats),
                  ],
                ),
                const SizedBox(height: 6),
                Row(
                  children: [
                    const Icon(Icons.location_on, size: 14, color: Color(0xFFE5C100)),
                    const SizedBox(width: 2),
                    Text('${car.distanceKm} km away - ${car.location}', style: const TextStyle(fontSize: 11, color: Colors.black84)),
                  ],
                ),
                const Divider(height: 16),
                // Price Box
                Row(
                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                  children: [
                    _buildCarRate('Per Hour', '₹${car.pricePerHour.toInt()}'),
                    _buildCarRate('Per Day', '₹${car.pricePerDay.toInt()}', isHighlight: true),
                    _buildCarRate('Per Km', '₹${car.pricePerKm.toInt()}'),
                  ],
                ),
                const SizedBox(height: 12),
                Row(
                  children: [
                    Expanded(
                      child: OutlinedButton(
                        onPressed: () => launchUrl(Uri.parse('tel:${car.ownerPhone}')),
                        style: OutlinedButton.styleFrom(foregroundColor: const Color(0xFF2B2B2B)),
                        child: const Text('Call Owner', style: TextStyle(fontWeight: FontWeight.bold, fontSize: 12)),
                      ),
                    ),
                    const SizedBox(width: 8),
                    Expanded(
                      child: ElevatedButton(
                        style: ElevatedButton.styleFrom(backgroundColor: const Color(0xFFFFD700), foregroundColor: Colors.black),
                        onPressed: () => _openRentCarBookingModal(car),
                        child: const Text('Rent Now', style: TextStyle(fontWeight: FontWeight.bold, fontSize: 12)),
                      ),
                    ),
                  ],
                ),
              ],
            ),
          ),
        ],
      ),
    );
  }

  void _openRentCarBookingModal(RentCarModel car) {
    int days = 1;
    bool withDriver = false;
    showModalBottomSheet(
      context: context,
      shape: const RoundedRectangleBorder(borderRadius: BorderRadius.vertical(top: Radius.circular(20))),
      builder: (ctx) => StatefulBuilder(
        builder: (ctx, setModalState) {
          final totalCost = (car.pricePerDay * days) + (withDriver ? 500 * days : 0);
          final advance = (totalCost * 0.20).clamp(300, 5000);

          return Padding(
            padding: const EdgeInsets.all(20),
            child: Column(
              mainAxisSize: MainAxisSize.min,
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Text('Rent ${car.carName}', style: const TextStyle(fontSize: 18, fontWeight: FontWeight.bold)),
                Text('${car.carModel} • ${car.carType}', style: const TextStyle(fontSize: 12, color: Colors.black54)),
                const SizedBox(height: 14),
                const Text('Select Duration (Days):', style: TextStyle(fontWeight: FontWeight.bold, fontSize: 13)),
                Row(
                  children: [1, 2, 3, 5, 7].map((d) {
                    final sel = days == d;
                    return Padding(
                      padding: const EdgeInsets.only(right: 8, top: 6),
                      child: ChoiceChip(
                        label: Text('${d}d'),
                        selected: sel,
                        selectedColor: const Color(0xFFFFD700),
                        onSelected: (val) => setModalState(() => days = d),
                      ),
                    );
                  }).toList(),
                ),
                const SizedBox(height: 12),
                Row(
                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                  children: [
                    const Text('Drive Option:'),
                    Switch(
                      value: withDriver,
                      activeColor: const Color(0xFFFFD700),
                      onChanged: (val) => setModalState(() => withDriver = val),
                    ),
                    Text(withDriver ? 'With Driver (+₹500/d)' : 'Self Drive'),
                  ],
                ),
                const Divider(),
                Row(
                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                  children: [
                    const Text('Total Rental Cost:', style: TextStyle(fontWeight: FontWeight.bold)),
                    Text('₹${totalCost.toInt()}', style: const TextStyle(fontWeight: FontWeight.w900, fontSize: 16)),
                  ],
                ),
                Row(
                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                  children: [
                    const Text('Advance via Razorpay (20%):', style: TextStyle(color: Colors.green, fontWeight: FontWeight.bold)),
                    Text('₹${advance.toInt()}', style: const TextStyle(color: Colors.green, fontWeight: FontWeight.bold)),
                  ],
                ),
                const SizedBox(height: 16),
                ElevatedButton(
                  style: ElevatedButton.styleFrom(
                    backgroundColor: const Color(0xFFFFD700),
                    foregroundColor: Colors.black,
                    minimumSize: const Size.fromHeight(44),
                  ),
                  onPressed: () async {
                    Navigator.pop(ctx);
                    final paise = (advance * 100).toInt();
                    final url = Uri.parse('https://razorpay.me/@bharatmitrainfotech?amount=$paise');
                    await launchUrl(url, mode: LaunchMode.externalApplication);
                    if (mounted) {
                      ScaffoldMessenger.of(context).showSnackBar(
                        SnackBar(content: Text('Rental confirmed for ${car.carName}! Advance paid.')),
                      );
                    }
                  },
                  child: Text('Pay Advance ₹${advance.toInt()} via Razorpay', style: const TextStyle(fontWeight: FontWeight.bold)),
                ),
              ],
            ),
          );
        },
      ),
    );
  }

  Widget _buildSpecChip(String text) {
    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 6, vertical: 2),
      decoration: BoxDecoration(color: const Color(0xFFF0F0F0), borderRadius: BorderRadius.circular(4)),
      child: Text(text, style: const TextStyle(fontSize: 10, fontWeight: FontWeight.bold, color: Colors.black87)),
    );
  }

  Widget _buildCarRate(String label, String value, {bool isHighlight = false}) {
    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 6),
      decoration: BoxDecoration(
        color: isHighlight ? const Color(0xFFFFF9DB) : const Color(0xFFFAFAFA),
        borderRadius: BorderRadius.circular(6),
        border: Border.all(color: isHighlight ? const Color(0xFFFFD700) : Colors.black12),
      ),
      child: Column(
        children: [
          Text(value, style: TextStyle(fontWeight: FontWeight.w900, fontSize: 13, color: isHighlight ? Colors.black : Colors.black87)),
          Text(label, style: const TextStyle(fontSize: 9, color: Colors.black54)),
        ],
      ),
    );
  }

  // =================== HIRE DRIVER / HIRE TECHNICIAN WALL ===================
  Widget _buildDriverOrTechnicianWall() {
    final filtered = _dummyProviders.where((p) {
      if (widget.tabIndex == 0 && !p.profession.contains('Driver')) return false;
      if (widget.tabIndex == 2 && p.profession.contains('Driver')) return false;

      if (_searchQuery.trim().isEmpty) return true;
      final q = _searchQuery.toLowerCase();
      final words = q.split(' ').where((w) => w.isNotEmpty);
      return words.every((w) =>
          p.name.toLowerCase().contains(w) ||
          p.profession.toLowerCase().contains(w) ||
          p.vehicle.toLowerCase().contains(w) ||
          p.location.toLowerCase().contains(w));
    }).toList();

    return Column(
      children: [
        Padding(
          padding: const EdgeInsets.all(12),
          child: TextField(
            decoration: InputDecoration(
              hintText: widget.tabIndex == 0
                  ? "Search driver by name, vehicle e.g. 'Sedan Driver'..."
                  : "Search technician e.g. 'AC Mistri', 'Electrician'...",
              prefixIcon: const Icon(Icons.search, color: Color(0xFF2B2B2B)),
              filled: true,
              fillColor: Colors.white,
              contentPadding: const EdgeInsets.symmetric(horizontal: 16),
              border: OutlineInputBorder(borderRadius: BorderRadius.circular(12), borderSide: BorderSide.none),
            ),
            onChanged: (val) => setState(() => _searchQuery = val),
          ),
        ),
        Container(
          margin: const EdgeInsets.symmetric(horizontal: 12),
          padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 8),
          decoration: BoxDecoration(
            color: const Color(0xFFFFD700).withOpacity(0.15),
            borderRadius: BorderRadius.circular(8),
            border: Border.all(color: const Color(0xFFFFD700)),
          ),
          child: Row(
            children: [
              const Icon(Icons.verified, size: 16, color: Color(0xFF2B2B2B)),
              const SizedBox(width: 8),
              Expanded(
                child: Text(
                  widget.tabIndex == 0
                      ? 'Zero Commission Driver Hire: Call & book verified drivers directly with 0% cuts.'
                      : 'Zero Commission Technician Hire: Connect directly with skilled local mistris & electricians.',
                  style: const TextStyle(fontSize: 11, fontWeight: FontWeight.bold, color: Color(0xFF2B2B2B)),
                ),
              ),
            ],
          ),
        ),
        const SizedBox(height: 8),
        Expanded(
          child: ListView.separated(
            padding: const EdgeInsets.all(12),
            itemCount: filtered.length,
            separatorBuilder: (_, __) => const SizedBox(height: 12),
            itemBuilder: (context, index) {
              final provider = filtered[index];
              return _buildProviderCard(provider);
            },
          ),
        ),
      ],
    );
  }

  Widget _buildProviderCard(ProviderModel provider) {
    return Card(
      elevation: 2,
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(14)),
      child: Padding(
        padding: const EdgeInsets.all(14),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Row(
              children: [
                CircleAvatar(
                  backgroundColor: const Color(0xFF2B2B2B),
                  radius: 22,
                  child: Text(
                    provider.name.substring(0, 1),
                    style: const TextStyle(color: Color(0xFFFFD700), fontWeight: FontWeight.bold, fontSize: 18),
                  ),
                ),
                const SizedBox(width: 12),
                Expanded(
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Text(provider.name, style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 15)),
                      Text('${provider.profession} • ${provider.vehicle}', style: const TextStyle(fontSize: 12, color: Colors.black54)),
                      const SizedBox(height: 2),
                      Row(
                        children: [
                          const Icon(Icons.location_on, size: 14, color: Color(0xFFE5C100)),
                          const SizedBox(width: 2),
                          Text('${provider.distanceKm} km away - ${provider.location}', style: const TextStyle(fontSize: 11, color: Colors.black84)),
                        ],
                      ),
                    ],
                  ),
                ),
                Container(
                  padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 4),
                  decoration: BoxDecoration(
                    color: const Color(0xFFFFD700).withOpacity(0.2),
                    borderRadius: BorderRadius.circular(8),
                  ),
                  child: Row(
                    children: [
                      const Icon(Icons.star, size: 14, color: Color(0xFFE5C100)),
                      const SizedBox(width: 3),
                      Text('${provider.rating}', style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 12)),
                    ],
                  ),
                ),
              ],
            ),
            const Divider(height: 20),
            const Text('CUSTOM RATES (Directly Set by Provider):', style: TextStyle(fontSize: 10, fontWeight: FontWeight.bold, color: Colors.black45)),
            const SizedBox(height: 6),
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                _buildRateBadge('Visiting', '₹${provider.rates.visitingCharge.toInt()}'),
                _buildRateBadge('Hourly', '₹${provider.rates.hourlyRate.toInt()}/hr'),
                _buildRateBadge('Daily', '₹${provider.rates.dailyRate.toInt()}/day'),
                _buildRateBadge('Per Km', '₹${provider.rates.perKmRate.toInt()}/km'),
              ],
            ),
            const SizedBox(height: 12),
            Row(
              children: [
                Expanded(
                  child: OutlinedButton.icon(
                    icon: const Icon(Icons.call, size: 16),
                    label: const Text('Direct Call', style: TextStyle(fontSize: 12, fontWeight: FontWeight.bold)),
                    style: OutlinedButton.styleFrom(
                      foregroundColor: const Color(0xFF2B2B2B),
                      side: const BorderSide(color: Color(0xFF2B2B2B)),
                      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(8)),
                    ),
                    onPressed: () => launchUrl(Uri.parse('tel:${provider.phone}')),
                  ),
                ),
                const SizedBox(width: 8),
                Expanded(
                  child: ElevatedButton.icon(
                    icon: const Icon(Icons.bolt, size: 16),
                    label: const Text('Book Now', style: TextStyle(fontSize: 12, fontWeight: FontWeight.bold)),
                    style: ElevatedButton.styleFrom(
                      backgroundColor: const Color(0xFFFFD700),
                      foregroundColor: Colors.black,
                      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(8)),
                    ),
                    onPressed: () {
                      ScaffoldMessenger.of(context).showSnackBar(
                        SnackBar(content: Text('Direct booking initiated with ${provider.name}!')),
                      );
                    },
                  ),
                ),
              ],
            ),
          ],
        ),
      ),
    );
  }

  Widget _buildRateBadge(String label, String value) {
    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 4),
      decoration: BoxDecoration(
        color: const Color(0xFFF9F9F9),
        borderRadius: BorderRadius.circular(6),
        border: Border.all(color: Colors.black12),
      ),
      child: Column(
        children: [
          Text(value, style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 11)),
          Text(label, style: const TextStyle(fontSize: 9, color: Colors.black45)),
        ],
      ),
    );
  }
}
