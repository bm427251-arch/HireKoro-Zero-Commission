// Flutter models for HireKoro Zero Commission Platform

class CustomRateBox {
  final double visitingCharge;
  final double hourlyRate;
  final double dailyRate;
  final double perKmRate;

  CustomRateBox({
    this.visitingCharge = 150.0,
    this.hourlyRate = 120.0,
    this.dailyRate = 900.0,
    this.perKmRate = 14.0,
  });

  Map<String, dynamic> toMap() {
    return {
      'visitingCharge': visitingCharge,
      'hourlyRate': hourlyRate,
      'dailyRate': dailyRate,
      'perKmRate': perKmRate,
    };
  }

  factory CustomRateBox.fromMap(Map<String, dynamic> map) {
    return CustomRateBox(
      visitingCharge: (map['visitingCharge'] as num?)?.toDouble() ?? 150.0,
      hourlyRate: (map['hourlyRate'] as num?)?.toDouble() ?? 120.0,
      dailyRate: (map['dailyRate'] as num?)?.toDouble() ?? 900.0,
      perKmRate: (map['perKmRate'] as num?)?.toDouble() ?? 14.0,
    );
  }
}

class ProfessionModel {
  final String id;
  final String name;
  final String category;
  final int slabAssigned;
  final bool isEnabled;

  ProfessionModel({
    required this.id,
    required this.name,
    required this.category,
    required this.slabAssigned,
    this.isEnabled = true,
  });
}

class VehicleModel {
  final String id;
  final String name;
  final String category;
  final int slabAssigned;
  final String capacity;
  final bool isEnabled;

  VehicleModel({
    required this.id,
    required this.name,
    required this.category,
    required this.slabAssigned,
    required this.capacity,
    this.isEnabled = true,
  });
}

class ProviderModel {
  final String id;
  final String name;
  final String phone;
  final String profession;
  final String vehicle;
  final String location;
  final double distanceKm;
  final double rating;
  final int reviewCount;
  final bool isUnderReview;
  final CustomRateBox rates;

  ProviderModel({
    required this.id,
    required this.name,
    required this.phone,
    required this.profession,
    required this.vehicle,
    required this.location,
    required this.distanceKm,
    required this.rating,
    required this.reviewCount,
    this.isUnderReview = false,
    required this.rates,
  });
}

class RentCarModel {
  final String id;
  final String carName;
  final String carModel;
  final String carType; // Hatchback, Sedan, SUV, Luxury
  final String fuel; // Petrol, Diesel, CNG
  final String seats; // 5 Seater, 7 Seater
  final String transmission; // Manual, Automatic
  final String ownerType; // Individual, Agency
  final String ownerName;
  final String ownerPhone;
  final double pricePerHour;
  final double pricePerDay;
  final double pricePerKm;
  final bool withDriverAvailable;
  final bool selfDriveAvailable;
  final bool isAvailableNow;
  final String location;
  final double distanceKm;
  final double rating;
  final int reviewCount;

  RentCarModel({
    required this.id,
    required this.carName,
    required this.carModel,
    required this.carType,
    required this.fuel,
    required this.seats,
    required this.transmission,
    required this.ownerType,
    required this.ownerName,
    required this.ownerPhone,
    required this.pricePerHour,
    required this.pricePerDay,
    required this.pricePerKm,
    this.withDriverAvailable = true,
    this.selfDriveAvailable = true,
    this.isAvailableNow = true,
    required this.location,
    required this.distanceKm,
    required this.rating,
    required this.reviewCount,
  });
}
