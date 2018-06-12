# CITE Service

Output from eComparatio, for any significant text, is going to generate a very large CEX file, with tens of thousands of entries. `SCS` (the Scala Cite Service) is a server-side service that can handle large digital libraries.

Source code for this service is at: <https://github.com/cite-architecture/scs-akka>.


## Requirements

- Java 1.8

## Configuration

SCS looks for a CEX-formatted library at `cex/library.cex`. 

## Running

At a terminal prompt:

    $ java -jar scs.jar


## Examples

A full set of examples of service requests, aimed at a working installation of SCS delivering data from the [Homer Multitext 2018d Data Release]() is at <http://beta.hpcc.uh.edu/hmt/hmt-microservice/>. Below are some examples that will show SCS working with eComparatio data.

This assumes the service is running at `http://localhost:9000`.

### Text Passages

- A passage of text:
- A citation in every version of a text:
- A range of tokens:
- A range of tokens across two exemplars:

### eComparatio Difference Records

- Browsing difference records:
- Browsing difference records from a given point:
- Finding difference records with significant edit-distance:



