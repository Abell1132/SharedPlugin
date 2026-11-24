# Shared Plugin for DarkBot

This plugin has been created to make it easier for any developer who wants to implement their features and use them in [Darkbot](https://github.com/darkbot-reloaded/DarkBot).

This repository will always be open source and unlicensed.

We will seek to implement CI/CD in the repository so that developers have a good user experience and many of the tasks are automated.

## Current features

- Simple Healing: Activate the ship's healing ability and use the PET healing gear. By @do-gamer
- Ore Seller: Sells ores at base, via PET trader gear, or using the HM7 trade drone when cargo is full. By @do-gamer
- Solaris Ability: Activate Solaris (also Paladin) ability when there are a certain number of NPCs nearby. By @do-gamer
- SpaceBall: Attack SpaceBall without fleeing away and collect cargo boxes around the gate. By @do-gamer
- Fix PET stuck: Reloads the game when the PET window gets stuck. By @do-gamer
- Repair PET: Repairs your PET when its health drops below a certain threshold. By @do-gamer
- Close AD Offer: Automatically closes AD Offer on the left side (temporary solution until the bot is fixed). By @do-gamer
- Auto Refiner: Automatically refines materials (Prometid, Duranium, Promerium) when cargo reaches a specified threshold. By @kopoklesz
- Crowd Avoidance: Detects crowded areas around the ship and moves away from them. By @do-gamer
- GG Alert Closer: Closes alerts at Galaxy Gates (Hades, LoW and Kuiper). By @do-gamer & @orbithelper
- Fast Travel: Fast travel between maps using Jump CPU (AJP-01). By @do-gamer & @orbithelper
- Orbit Helper Quick Login: Adds a button to navigate to Orbit Helper from the menu. By @orbithelper
- Simple Galaxy Gate: Supports ABG, Delta, Epsilon, Zeta, Hades, Kuiper, LoW, Invasion, Mimesis, EBG, GoP, Treacherous, DSE, Fiesta, Trinity and Voyagers. By @do-gamer
- Autobuy: Automatically buys boosters and special items from the shop at a configured interval. By @do-gamer
- Log Overlay: Displays the latest in-game log messages on the canvas (top-center, auto-fade after 5s). Built-in keyword whitelist limits the displayed lines to gains and errors so the canvas does not get cluttered. By @Halizeur

## Contributing

Check the [CONTRIBUTING.md](CONTRIBUTING.md) file for more information.

## Disclaimer

The owner of this repository only facilitates other developers to test its functionalities and is not responsible for them.
If any feature violates any rules or licences, please contact me so that it can be deleted.

### Auto Dispatch

Enable the Auto Dispatch feature and its Enabled setting in the plugin GUI. Select
a retriever (default R-01, 200,000 credits) and a check interval (default 15 minutes,
range 1–1440). It runs while the bot is running, collects completed retrievers, and
fills automatically detected free unlocked slots. It never buys slots or instantly
completes dispatches. Other selected retrievers may cost Uridium and permits;
unavailable or unaffordable choices wait until the next check without substitution.
Each UI pass is limited to 120 seconds; failed hires wait until the next check.

Mechanics: [official Retriever & Gate Dispatch FAQ](https://board-en.darkorbit.com/threads/faq-retriever-gate-dispatch.127317/).
The feature uses the core DispatchManager for UI actions because DispatchAPI
provides data and selection but no hire/collect operations. Live UI behavior still
requires verification on dbdev with a logged-in account.
