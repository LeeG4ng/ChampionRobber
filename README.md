<div align="center">

# **ChampionRobber**
[中文](#中文) | [English](#English)

</div>

# 中文
支持英雄联盟极地大乱斗模式的抢英雄工具。

## 功能
- 在大乱斗模式中，当可选择英雄变动时，根据预配置的优先级秒抢英雄。
- (Future) 在匹配模式中，当对局进入英雄选择阶段时秒选预配置的英雄。

## 下载
[Release](https://github.com/LeeG4ng/ChampionRobber/releases)

## 使用
- 修改配置文件 `config.yml`，在 `aram.champions` 节点下定义优先级数组，从前往后优先级递减。
- 同一优先级下可定义多个英雄，使用逗号或空白符号分割英雄名，只有当可选英雄中出现优先级更高的英雄时才会进行替换。
- 配置英雄名暂时只支持中文英雄称号，例如“诡术妖姬”、“疾风剑豪”等。

# English
A champion robber tool which supports the ARAM mode in League of Legends.

## Features
- When selectable champions changed in ARAM mode, the champion will be selected promptly according to the preconfigured priority.
- (Future) In PvP mode, a preconfigured champion will be selected promptly when the match enters champSelect phase.

## Download
[Release](https://github.com/LeeG4ng/ChampionRobber/releases)

## Usage
- Modify the configuration file `config.yml` to define an array of priorities under the `aram.champions` node, decreasing priority from front to back.
- Multiple champions can be defined under the same priority, and the champion name can be separated by commas or blank symbols. A champion with a higher priority will be replaced.
- Configuring champion names only supports Chinese champion titles, such as "诡术妖姬", "疾风剑豪", etc.
