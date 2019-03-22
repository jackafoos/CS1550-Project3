*jem319*
*CS1550*
# Project 3 Analysis

## Aging and Refresh
![Graphing Page Faults by Refresh Rate](RefreshByPageFaults.png)

Picking a good refresh rate left me with a few options. The dips in page faults
left occurred in 2 places fairly consistently, with varying degrees of effectiveness.

1. `-r 10` is the more significant dip for 2/3 of the trace files. gzip.trace had a much less
significant change.
1. `-r 20` is the other solid choice. All 3 trace files dip here with one extreme change
with gzip.trace. As the aging algorithm is not always about optimizing, I think it is best
to go with this option as the average number of page faults is much lower than the rest.

## Frames and Page Faults

Here are the statistics for running each algorithm on each trace file provided.
I will be using Opt as a baseline measure in which to discuss Fifo and Aging.
For Aging, I will be using `./vmsim -n <FRAMES> -a aging -r 20 tracefile` as discussed in the above section.

### swim.trace

|    Frames    |     Opt     |     Fifo      |     Aging     |
|------------- |------------ | ------------- | ------------- |
|8|4803|117905|70950|
|16|373|117905|63386|
|32|147|117905|44786|
|64|135|117905|2463|

### gzip.trace

|    Frames    |     Opt     |     Fifo      |     Aging     |
|------------- |------------ | ------------- | ------------- |
|8|39881|319697|40259|
|16|39864|319697|39907|
|32|39864|319697|39857|
|64|39864|319697|39857|

### gcc.trace

|    Frames    |     Opt     |     Fifo      |     Aging     |
|------------- |------------ | ------------- | ------------- |
|8|14293|190684|159964|
|16|3273|190684|144684|
|32|502|190684|**67332**|
|64|327|190684|**93810**|

### Conclusion
The Most useful algorithm in an actual operating system using this data would have to be
an **Aging** algorithm. Opt is clearly the best performing algorithm in terms of page faults, but
it is slow and clunky and it is impractical to use. Fifo gets very bad performance and the number of frames doesn't improve the number of page faults. It is consistently lower than Aging.

## Fifo and Belady's Anomaly
There is one instance of Belady's anomaly in my results. Under the Aging column of the
**gcc.trace** section at 32 frames the number of page faults was 67,332. but when increasing
the amount of memory to 64 frames, the number of page faults increased to 93,810.
