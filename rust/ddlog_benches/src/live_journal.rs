use benchmarks_ddlog::{typedefs::live_journal::Edge, Relations};
use differential_datalog::{
    ddval::{DDValConvert, DDValue},
    program::{RelId, Update},
};
use flate2::bufread::GzDecoder;
use std::{
    fs::File,
    io::{BufRead, BufReader},
};

pub fn dataset(samples: Option<usize>) -> Vec<Update<DDValue>> {
    // Grab the file and wrap it in a buffered reader so it doesn't take forever
    // to read the entire thing in one go
    let reader = BufReader::new(GzDecoder::new(BufReader::new(
        File::open(concat!(
            env!("CARGO_MANIFEST_DIR"),
            "/data/soc-LiveJournal1.txt.gz",
        ))
        .expect("could not open data file"),
    )));

    let mut edges = Vec::with_capacity(samples.unwrap_or(40_000_000));

    for line in reader
        .lines()
        .flat_map(|line| line.ok())
        .take(samples.unwrap_or_else(usize::max_value))
    {
        if line.starts_with('#') {
            continue;
        }

        let mut split = line.trim().split('\t');
        let from = split.next().unwrap().trim().parse().unwrap();
        let to = split.next().unwrap().trim().parse().unwrap();

        edges.push(Update::Insert {
            relid: Relations::live_journal_Edge as RelId,
            v: Edge { from, to }.into_ddvalue(),
        });
    }

    edges
}
