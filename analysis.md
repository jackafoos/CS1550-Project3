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

    Frames    |     Opt     |     Fifo      |     Aging
------------- |------------ | ------------- | -------------
8|0|0|0
16|0|0|0
32|0|0|0
64|0|0|0

### gzip.trace

    Frames    |     Opt     |     Fifo      |     Aging
------------- |------------ | ------------- | -------------
8|0|0|0
16|0|0|0
32|0|0|0
64|0|0|0

### gcc.trace

    Frames    |     Opt     |     Fifo      |     Aging
------------- |------------ | ------------- | -------------
8|0|0|0
16|0|0|0
32|0|0|0
64|0|0|0

## Fifo and Belady's Anomaly
