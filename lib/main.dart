import 'dart:developer';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_app_icon_badge/flutter_app_icon_badge.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      home: const MyHomePage(),
    );
  }
}

class MyHomePage extends StatefulWidget {
  const MyHomePage({Key? key}) : super(key: key);

  @override
  State<MyHomePage> createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  static const platform = MethodChannel('com.listta.goals/counter');

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Center(
        // Center is a layout widget. It takes a single child and positions it
        // in the middle of the parent.
        child: Center(
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              ElevatedButton(
                onPressed: () {
                  _setCounterByNative(5);
                },
                child: const Text('Set by Native 5'),
              ),
              const SizedBox(width: 16),
              ElevatedButton(
                onPressed: () {
                  _setCounterByNative(0);
                },
                child: const Text('Remove by Native'),
              ),
              const SizedBox(width: 16),
              ElevatedButton(
                onPressed: () {
                  _setCounterByFlutterLib(25);
                },
                child: const Text('Set by Flutter 25'),
              ),
              const SizedBox(width: 25),
              ElevatedButton(
                onPressed: () {
                  _setCounterByFlutterLib(0);
                },
                child: const Text('Remove by flutter'),
              ),
            ],
          ),
        ),
      ),
    );
  }

  void _setCounterByFlutterLib(int counter) {
    FlutterAppIconBadge.updateBadge(counter);
  }

  void _setCounterByNative(int counter) async {
    try {
      platform.invokeMethod('setCounter', {"counter": counter});
    } on PlatformException catch (e) {
      log("Failed to set counter: '${e.message}'.");
    }
  }
}
