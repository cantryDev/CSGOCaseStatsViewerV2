# CS:GO case stats viewer

## How does it work?

This Java Application loads your whole CS:GO <a href=https://steamcommunity.com/my/inventoryhistory>inventory history</a> by emulating the load more history button.
To avoid rate limiting it loads 50 transactions (1 Http request) per ~3.5 seconds. Which mean if you have a large history it will take longer to load.
After the inventory history is dumped you can analyse how many cases you opened.

## Usage

1. Download the latest compiled version as zip via the <a href=https://github.com/cantryDev/CSGOCaseStatsViewerV2/releases/latest>latest release tab</a> or compile it yourself with maven.
2. Unzip the downloaded zip
3. Execute the execute.bat 
4. Follow the steps in the commandline which just got opened
5. Get disappointed.

## Example result

<a href=https://github.com/cantryDev/CSGOCaseStatsViewerV2/blob/master/result_07_02_2021_14_54.txt>click</a>

## Requirements
- Java 11 or higher

## Donate
It took a lot of time and energy drinks to develop this tool.
If this tool was helpful consider donating a case or two.

Steam: <a href="https://steamcommunity.com/tradeoffer/new/?partner=58001078&token=jeCI_XHm">tradelink</a>

## FAQ

### Migrate from v1
You can migrate your data from the old version.
Just copy your dumps folder here and rename it to data.

### It crashed
Just restart it. It will continue where it crashed.

### Why does it need my cookies?
It needs your cookies to request your <a href=https://steamcommunity.com/my/inventoryhistory>inventory history</a>

### How do I get my cookies?

#### Chrome
1. Login into your steam account.
2. Visit your steam profile.
3. Press F12 or right click(anywhere on the website) and press inspect element. 
4. Select the network tab
5. Reload the page
6. Go back to the network tab and scroll to the top and select the first entry. Click on headers. On the right side it should say Request Url: Your steam profile url
7. Scroll down on the right side till you see Cookie:
8. Right click on Cookie: and select copy value. Or copy it manual.

#### Firefox
1. Login into your steam account.
2. Visit your steam profile.
3. Press F12 or right click(anywhere on the website) and press inspect element.
4. Select the network tab
5. Reload the page
6. Go back to the network tab and scroll to the top and select the first entry. On the right side it should say GET: Your steam profile url
7. Scroll down on the right side till you see Cookie:
8. Right click on Cookie: and select copy
