# Living Atlas Pipelines extensions

This project is **proof of concept quality code** aimed at identifying work required
 to use of pipelines as a replacement to [biocache-store](https://github.com/AtlasOfLivingAustralia/biocache-store)
 for data ingress. 

## Architecture

For details on the GBIF implementation, see the [pipelines github repository](https://github.com/gbif/pipelines).
This project is focussed on extension to that architecture to support use by the living atlases.

![Pipelines](https://docs.google.com/drawings/d/e/2PACX-1vQhQSg5VFo2xRZfDhmvhKuNLUpyTOlW-t-m1fesJ2RElWorVPAEbnsZg_StJKh22mEcS4D28j_nPoTV/pub?w=960&h=720 "Pipelines") 

Above is a representation of the data flow from source data in Darwin core archives supplied by data providers, to the API access to these data via the biocache-service component.

Within the "Interpreted AVRO" box is a list of "transforms" each of which take the source data and produce an isolated output in a AVRO formatted file.

[GBIF's pipelines](https://github.com/gbif/pipelines) already supports a number of core transforms for handling biodiversity occurrence data. The intention is to make us of these transforms "as-is" which effectively perform the very similar functionality to what is supported by the biocache-store project (ALA's current ingress library for occurrence data). 

This list of transforms will need to be added to backfill some of the ingress requirements of the ALA. These transforms will make use of existing ALA services:

* *ALA Taxonomy transform* - will make use of the existing **[ala-name-matching](https://github.com/AtlasOfLivingAustralia/ala-name-matching) library**
* *Sensitive data* - will make use of existing services in https://lists.ala.org.au to retrieve sensitive species rules.
* *Spatial layers* - will make use of existing services in https://spatial.ala.org.au/ws/ to retrieve sampled environmental and contextual values for geospatial points
* *Species lists* - will make use of existing services in https://lists.ala.org.au to retrieve species lists.

In addition pipelines for following will need to be developed:

* Duplicate detection
* Environmental outlier detection
* Expert distribution outliers

## Prototyped so far:

1. Pipeline extension to add the ALA taxonomy to the interpreted data
2. Extension with Sampling information (Environmental & Contextual)
3. Generation of search SOLR index compatible with biocache-service

## To be done:

1. Sensible use of GBIF's key/value store framework (backend storage to be identified)
2. Dealing with sensitive data
3. Integration with Collectory - ALA's current production metadata registry
4. Integration with Lists tool
5. Extensions with separate taxonomies e.g. NZOR
6. Handling of images with ALA's image-service as storage

## Dependent projects

The pipelines work will necessitate some minor additional API additions and change to the following components:

#### biocache-service
[experimental/pipelines branch](https://github.com/AtlasOfLivingAustralia/biocache-service/tree/experimental/pipelines) 
The aim for this proof of concept is to make very minimal changes to biocache-service, maintain the existing API and have no impact on existing services and applications.

#### ala-namematching-service
A simple **drop wizard wrapper around the [ala-name-matching](https://github.com/AtlasOfLivingAustralia/ala-name-matching) library** has been prototyped to support integration with pipelines.
 
#### spatial-service
[intersect-cache branch](https://github.com/AtlasOfLivingAustralia/spatial-service/tree/intersect-cache) An additional webservice to allow a point
lookup for all layers. This needs to be supplement with additional work to populate a key value store cache using the spatial-service batch API.


## Getting started

In the absence of ansible scripts, here are some instructions for setting up a local development environment for pipelines.
These steps will load a dataset in SOLR.

1. Run `git clone https://github.com/djtfmartin/la-pipelines`
1. Build with maven `mvn clean install`
1. Run `git clone https://github.com/djtfmartin/ala-namematching-service`
1. Download name matching index from here:  `https://archives.ala.org.au/archives/nameindexes/latest/namematching-20190213.tgz` and unzip to `/data/lucene` and rename `/data/lucene/namematching-20190213` to `/data/lucene/namematching`
1. Create directory `/data/ala-namematching-service/config`
1. Copy JSON files from https://github.com/djtfmartin/ala-namematching-service/tree/master/src/main/resources to this directory
1. Run with `nohup ala-namematching-service/run.sh  &`
1. Download shape files from [here](https://pipelines-shp.s3-ap-southeast-2.amazonaws.com/pipelines-shapefiles.zip) and expand into `/data/pipelines-shp` directory
1. Download a darwin core archive (e.g. https://archives.ala.org.au/archives/gbif/dr893/dr893.zip) and expand it into `/data/biocache-load` e.g. `/data/biocache-load/dr893`
1. Create `/data/pipelines-data` directory
1. To convert DwCA to AVRO, run `./dwca-avro.sh dr893`
1. To interpret, run `./interpret.sh dr893`
1. To sample run
    1. ./export-latlng.sh dr893
    1. ./sample.sh dr893
    1. ./sample-cache.sh dr893
1. To setup SOLR:
    1. Install docker
    1. Follow the instructions in [solr/docker/README.md](solr/docker/README.md)
    1. Run `docker-compose -f solr.yml start`
    1. Run `./upload-solr-config.sh`
1. To index, run `./index.sh dr893`
