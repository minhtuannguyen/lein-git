# lein-git

A [Leiningen](https://github.com/technomancy/leiningen) plugin to make structural commit message.

[![Build Status](https://travis-ci.org/minhtuannguyen/lein-git.svg?branch=master)](https://travis-ci.org/minhtuannguyen/lein-git)
[![Clojars Project](https://img.shields.io/clojars/v/lein-git.svg)](https://clojars.org/lein-git)

## Usage
For project-level plugins:

Put `[lein-git "0.1.0-SNAPSHOT"]` into the `:plugins` vector of your project.clj.

You must specify the the commit message pattern by defining `:lein-git-spec` in the project.clj.
 
i.e `:lein-git-spec [[:story-id :required] [:software-component :optional]]`

`:required` means that the field must be entered in order to commit
`:optional` can be skipped 

To commit:

    #//Assuming :lein-git-spec [[:story-id :required] [:software-component :optional]]
    $ lein git commit -m "refactor add2basket functionality"
    The commit will follow the pattern :lein-git-spec [[:story-id :required] [:software-component :optional] ]
    
    Please enter the story-id
    JIRA-1234
    
    Please enter the software-component
    Order-System
        
    Commited with the message: [ [JIRA-1234] [Order-System] [refactor add2basket functionality] ]
    

To search:

    #Assuming [[:story-id :required] [:software-component :optional]] 
    #In interactive mode 
    $ lein git search -i
    All commits follow the spec:  [[:story-id :required] [:software-component :optional]]
    
    Select the field you want to search for: 
    **** :story-id -> (Enter 0)
    **** :software-component -> (Enter 1)
    0
    
    Enter your query:
    JIRA-4
    
    RESULT FOR YOUR QUERY  :story-id = JIRA-4
    
    |       :story-id | :software-component | :commit-msg |                                  :commit |
    |-----------------+---------------------+-------------+------------------------------------------|
    |    [JIRA-4]     |              [view] |        [hi] | f4f8f0fe6423e9fd206f4cb5bab6f0dc1e27d5ea |
    |    [JIRA-4]     |             [order] |        [hi] | 7f6d85e424e3a15ee34b17df4587c78ab10465ac |
    |    [JIRA-4]     |              [view] |        [hi] | 4075b70524d9ea63eeaa70ea16eac0264680857d |
    
    #Alternativ in query mode
    $ lein git search -q "story-id = JIRA-4"
    


## License

Copyright © 2016 

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
