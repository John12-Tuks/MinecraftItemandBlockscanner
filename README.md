# Minecraft Diamond Scanner 💎

An external Minecraft world-scanning tool written in **Java** that analyzes Minecraft region files (`.mca`) to locate selected blocks and ores without requiring the player to manually explore the world.

The project focuses on understanding and parsing Minecraft's **Anvil region format**, **NBT data**, and Minecraft chunk storage in order to extract useful world information from saved worlds.

> **Current Status:** 🟡 In Development

---

## 📌 Project Overview

The Minecraft Diamond Scanner is designed to scan Minecraft world data externally.

Instead of running inside Minecraft as a mod, the scanner works directly with the world's saved region files:

```text
Minecraft World
      │
      ▼
region/
      │
      ▼
r.x.z.mca
      │
      ▼
Chunk Data
      │
      ▼
NBT Data
      │
      ▼
Block / Section Information
      │
      ▼
Target Ore Detection
      │
      ▼
Coordinates / Results
```

The initial objective is to locate **diamonds**, but the system is being designed so that other blocks and ores can also be searched for.

---

# 🎯 Main Objectives

* Read Minecraft `.mca` region files.
* Identify and extract individual chunks.
* Decompress chunk data.
* Parse Minecraft's NBT format.
* Navigate nested NBT compounds.
* Identify block and world data.
* Detect selected ores or blocks.
* Convert discovered data into useful coordinates.
* Provide a GUI for selecting what to scan for.
* Eventually allow large regions of a Minecraft world to be scanned efficiently.

---

# 🛠️ Technology Stack

| Component            | Technology                         |
| -------------------- | ---------------------------------- |
| Programming Language | Java                               |
| File Format          | Minecraft Anvil `.mca`             |
| Data Format          | NBT                                |
| Compression          | Zlib / Minecraft chunk compression |
| Interface            | GUI                                |
| Target Data          | Minecraft world saves              |
| Architecture         | External scanner                   |

---

# 🧠 Development Plan

The project is being developed in stages rather than attempting to build the entire scanner at once.

## Phase 1: Understand the Minecraft Region Format

### Objective

Understand how Minecraft stores chunks inside `.mca` region files.

A region file contains up to:

```text
32 × 32 = 1024 chunks
```

Each chunk has an entry in the region file's location table indicating where its data is stored.

### Status

* [x] Investigate `.mca` files
* [x] Understand region/chunk relationship
* [x] Identify chunk location information
* [x] Understand that chunks are stored using sectors
* [ ] Build complete region-file reader

---

# Phase 2: Read Binary Data

The next stage is converting the raw bytes inside the region file into meaningful structures.

The scanner needs to understand values such as:

```text
00 → TAG_End
01 → TAG_Byte
02 → TAG_Short
03 → TAG_Int
04 → TAG_Long
05 → TAG_Float
06 → TAG_Double
07 → TAG_Byte_Array
08 → TAG_String
09 → TAG_List
0A → TAG_Compound
0B → TAG_Int_Array
0C → TAG_Long_Array
```

### Current Understanding

NBT data is essentially a structured hierarchy of tags.

For example:

```text
TAG_Compound
 ├── TAG_String
 ├── TAG_Int
 ├── TAG_Compound
 │    ├── TAG_String
 │    └── TAG_List
 └── TAG_End
```

The reader therefore needs to understand both the **tag type** and its **payload**.

---

# Phase 3: Build the NBT Reader

A major component of the project is a custom NBT parser.

The basic idea is:

```java
readTag()
    ↓
identify tag type
    ↓
read tag payload
    ↓
if TAG_Compound
    ↓
recursively read child tags
```

For example:

```text
TAG_Compound
      │
      ├── TAG_String
      │
      ├── TAG_Int
      │
      └── TAG_Compound
              │
              ├── TAG_String
              └── TAG_List
```

The reader will use recursion to navigate nested compounds.

### Current Progress

* [x] Understand NBT tag IDs
* [x] Understand tag payloads
* [x] Understand `TAG_End`
* [x] Understand `TAG_Compound`
* [x] Understand `TAG_String`
* [x] Understand primitive tag structures
* [x] Identify the need for recursive compound parsing
* [ ] Complete generic NBT parser
* [ ] Build Java representations of NBT tags
* [ ] Validate parser against real Minecraft data

---

# Phase 4: Extract Chunk Information

Once NBT parsing is working, the scanner will move from simply reading NBT to understanding Minecraft chunk data.

The scanner needs to locate the relevant structures inside the chunk NBT.

The intended process is:

```text
Region File
     ↓
Chunk
     ↓
Compressed NBT
     ↓
NBT Parser
     ↓
Chunk Data
     ↓
Sections
     ↓
Block Information
```

### Goal

Determine how Minecraft represents blocks within modern chunk data and correctly interpret their positions.

---

# Phase 5: Ore Detection

After successfully reading block information, the scanner can search for specific targets.

Example:

```text
Target:
minecraft:diamond_ore
```

The scanner could eventually support:

```text
minecraft:diamond_ore
minecraft:deepslate_diamond_ore
minecraft:ancient_debris
minecraft:iron_ore
minecraft:gold_ore
minecraft:emerald_ore
```

The goal is to avoid hard-coding the scanner around diamonds.

Instead:

```text
User selects target
        ↓
Scanner searches block data
        ↓
Matching blocks identified
        ↓
Coordinates calculated
        ↓
Results displayed
```

---

# Phase 6: Coordinate Reconstruction

Finding an ore is not enough.

The scanner needs to determine its actual:

```text
X
Y
Z
```

coordinates.

Minecraft data is divided into:

```text
Region
   ↓
Chunk
   ↓
Section
   ↓
Block
```

The scanner therefore needs to reconstruct the world coordinates from the block's location within these structures.

This is one of the important technical stages because incorrect coordinate calculations could make an otherwise successful scan useless.

---

# Phase 7: GUI

After the underlying scanner works, a GUI will be added.

The intended interface will allow the user to select:

```text
World
 └── Region / folder

Target:
 ├── Diamond
 ├── Ancient Debris
 ├── Iron
 ├── Gold
 └── Custom Block

[ Scan ]

Results:
X: ...
Y: ...
Z: ...
```

The GUI should act as the front end while the region-file/NBT scanner remains the underlying engine.

---

# Phase 8: Performance Improvements

Once the scanner is functional, performance will become a major focus.

Potential improvements include:

* Scanning multiple region files.
* Parallel chunk processing.
* Avoiding unnecessary NBT parsing.
* Streaming region files instead of loading everything into memory.
* Caching frequently accessed information.
* Filtering chunks before expensive processing.
* Supporting large Minecraft worlds.

The long-term goal is to make scanning significantly faster than manually searching the world.

---

# 🏗️ Current Architecture

The intended architecture is roughly:

```text
┌──────────────────────┐
│         GUI          │
│ Target / World Input │
└──────────┬───────────┘
           │
           ▼
┌──────────────────────┐
│    Scanner Engine    │
└──────────┬───────────┘
           │
           ▼
┌──────────────────────┐
│   Region File Reader │
│       (.mca)         │
└──────────┬───────────┘
           │
           ▼
┌──────────────────────┐
│    Chunk Reader      │
└──────────┬───────────┘
           │
           ▼
┌──────────────────────┐
│     NBT Parser       │
└──────────┬───────────┘
           │
           ▼
┌──────────────────────┐
│   Block Extraction   │
└──────────┬───────────┘
           │
           ▼
┌──────────────────────┐
│    Ore Detection     │
└──────────┬───────────┘
           │
           ▼
┌──────────────────────┐
│ Coordinate Results   │
└──────────────────────┘
```

---

# ✅ Current Accomplishments

The project has progressed beyond the initial idea stage.

### Minecraft Storage Research

* [x] Investigated Minecraft region files.
* [x] Understand that `.mca` files contain chunks.
* [x] Understand the 32×32 chunk structure of a region.
* [x] Investigated the binary structure of chunk data.
* [x] Started working directly with hexadecimal/binary data.

### NBT Research

* [x] Identified NBT tag IDs.
* [x] Understand the concept of NBT payloads.
* [x] Understand `TAG_String`.
* [x] Understand `TAG_Compound`.
* [x] Understand `TAG_End`.
* [x] Understand that compounds contain nested tags.
* [x] Determined that recursive parsing is appropriate for nested compounds.
* [x] Started interpreting real Minecraft NBT data.

### Scanner Design

* [x] Established an external-tool architecture.
* [x] Selected Java as the implementation language.
* [x] Established `.mca` files as the primary data source.
* [x] Designed the scanner around selectable targets rather than diamonds alone.
* [x] Identified the need for a GUI.
* [x] Identified coordinate reconstruction as a required stage.

---

# 🚧 Current Development Stage

The project is currently transitioning from **Minecraft file-format research** into **implementation of the actual parser/scanner**.

The biggest current technical milestone is:

> **Build a reliable NBT reader that can recursively parse Minecraft's binary NBT structures.**

Once this is working reliably, the project can move toward extracting actual chunk/block information.

---

# 🗺️ Roadmap

```text
[x] Research Minecraft region files
[x] Understand .mca structure
[x] Investigate binary/hex data
[x] Research NBT
[x] Understand NBT tags
[x] Understand NBT payloads
[x] Design recursive NBT parsing
[ ] Implement NBT reader
[ ] Implement region reader
[ ] Read compressed chunk data
[ ] Extract chunk sections
[ ] Extract block states
[ ] Detect target ores
[ ] Calculate world coordinates
[ ] Build scanner engine
[ ] Build GUI
[ ] Add multi-region scanning
[ ] Optimize performance
[ ] Testing
[ ] Release
```

---

# 💎 Long-Term Goal

The final version should allow a user to provide a Minecraft world and search it externally for specific blocks.

For example:

```text
Minecraft World
      ↓
Select World
      ↓
Select "Diamond Ore"
      ↓
Scan
      ↓
Region Files
      ↓
Chunks
      ↓
NBT
      ↓
Block States
      ↓
Diamond Detection
      ↓
Coordinates
```

The ultimate objective is a **general-purpose Minecraft world data scanner**, with diamond detection being the first major use case.

---

# ⚠️ Important Technical Challenge

Minecraft's internal storage format changes between Minecraft versions.

Therefore, the scanner should not assume that every Minecraft version stores block data identically.

Version compatibility will eventually need to be considered when interpreting:

* Chunk NBT
* Block states
* Palettes
* Sections
* Compression
* World height
* Data version

This makes the project more than simply searching a file for the string `diamond_ore`.

The scanner needs to understand **how Minecraft encodes the world**.

---

# 📚 Project Philosophy

The project is being developed from the bottom up.

Rather than relying entirely on an existing Minecraft library, the development process focuses on understanding the underlying data structures:

```text
Raw Bytes
   ↓
Binary Structures
   ↓
NBT
   ↓
Chunks
   ↓
Sections
   ↓
Blocks
   ↓
Coordinates
```

This approach provides a deeper understanding of Minecraft's world-storage architecture and creates a foundation for future tools beyond the original diamond scanner.

---

## Status

**Development Status:** 🟡 Active Development

**Language:** Java

**Primary Objective:** Minecraft world-file scanning

**First Target:** Diamond Ore 💎

**Architecture:** External `.mca` / NBT scanner

**Current Focus:** NBT parser and binary Minecraft data interpretation
