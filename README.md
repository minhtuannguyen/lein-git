# lein-git

A [Leiningen](https://github.com/technomancy/leiningen) plugin which has been made in order to make commit whose messages follow certain pattern.

[![Build Status](https://travis-ci.org/minhtuannguyen/lein-git.svg?branch=master)](https://travis-ci.org/minhtuannguyen/lein-git)
[![Clojars Project](https://img.shields.io/clojars/v/lein-git.svg)](https://clojars.org/lein-git)
## Usage

Use this for user-level plugins:

Put `[lein-git "0.1.0-SNAPSHOT"]` into the `:plugins` vector of your `:user`
profile.

Use this for project-level plugins:

Put `[lein-git "0.1.0-SNAPSHOT"]` into the `:plugins` vector of your project.clj.

To commit:

    $ lein git commit

## License

Copyright © 2016 

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
