package com.example.mad_project;

import java.util.ArrayList;
import java.util.List;

/**
 * Data-only model for one backend roadmap topic screen.
 * mirrors how your existing topic screens pass data to LearningModuleActivity.
 */
public class BackendModule {

    // ── Topic screen metadata ─────────────────────────────────────────────────
    public final String title;
    public final String subtitle;
    public final String overviewTitle;
    public final String overviewBody;
    public final String prefKey;          // SharedPreferences key for section-done flag

    // ── Sub-topics ────────────────────────────────────────────────────────────
    public final List<SubTopic> subTopics;

    private BackendModule(Builder b) {
        this.title         = b.title;
        this.subtitle      = b.subtitle;
        this.overviewTitle = b.overviewTitle;
        this.overviewBody  = b.overviewBody;
        this.prefKey       = b.prefKey;
        this.subTopics     = b.subTopics;
    }

    // ── SubTopic inner class ──────────────────────────────────────────────────

    public static class SubTopic {
        public final String number;      // "01", "02" …
        public final String title;
        public final String description;
        public final String prefKey;     // individual completion key
        public final List<Resource> resources;

        public SubTopic(String number, String title, String description,
                        String prefKey, List<Resource> resources) {
            this.number      = number;
            this.title       = title;
            this.description = description;
            this.prefKey     = prefKey;
            this.resources   = resources;
        }
    }

    public static class Resource {
        public final String type;   // "Course" | "Official" | "Video"
        public final String label;
        public final String url;

        public Resource(String type, String label, String url) {
            this.type  = type;
            this.label = label;
            this.url   = url;
        }
    }

    // ── Builder ───────────────────────────────────────────────────────────────

    public static class Builder {
        String title, subtitle, overviewTitle, overviewBody, prefKey;
        final List<SubTopic> subTopics = new ArrayList<>();

        public Builder meta(String title, String subtitle,
                            String overviewTitle, String overviewBody, String prefKey) {
            this.title         = title;
            this.subtitle      = subtitle;
            this.overviewTitle = overviewTitle;
            this.overviewBody  = overviewBody;
            this.prefKey       = prefKey;
            return this;
        }

        public Builder topic(String number, String title, String desc, String prefKey,
                             String[][] resources) {
            List<Resource> resList = new ArrayList<>();
            for (String[] r : resources) resList.add(new Resource(r[0], r[1], r[2]));
            subTopics.add(new SubTopic(number, title, desc, prefKey, resList));
            return this;
        }

        public BackendModule build() { return new BackendModule(this); }
    }

    // =========================================================================
    //  STATIC FACTORY — all 9 backend modules defined here in one place
    // =========================================================================

    public static BackendModule get(String moduleKey) {
        switch (moduleKey) {
            case "internet":       return buildInternet();
            case "frontend_basic": return buildFrontendBasics();
            case "pick_language":  return buildPickLanguage();
            case "vcs":            return buildVcs();
            case "relational_db":  return buildRelationalDB();
            case "apis":           return buildApis();
            case "caching":        return buildCaching();
            case "web_servers":    return buildWebServers();
            default: throw new IllegalArgumentException("Unknown module: " + moduleKey);
        }
    }

    // ── 1. Internet ──────────────────────────────────────────────────────────

    private static BackendModule buildInternet() {
        return new Builder()
            .meta("Internet",
                  "How the internet works",
                  "The Internet",
                  "The internet is a global network of computers connected using standardized " +
                  "protocols. Understanding how data travels from one computer to another — " +
                  "through packets, routers, and protocols — is the essential foundation for " +
                  "any backend developer.",
                  "be_internet_done")
            .topic("01", "How does the Internet work?",
                "Data travels as small packets routed through interconnected computers. Learn " +
                "about IP addresses, routers, TCP/IP, and the client-server model. Understand " +
                "what happens from the moment you hit Enter on a URL to when a page loads.",
                "be_int_how",
                new String[][]{
                    {"Course",   "CS50 – Intro to Computer Science",     "https://cs50.harvard.edu/x/"},
                    {"Video",    "Khan Academy – How the Internet Works", "https://www.khanacademy.org/computing/computers-and-internet"},
                    {"Official", "MDN – How the Web Works",              "https://developer.mozilla.org/en-US/docs/Learn/Getting_started_with_the_web/How_the_Web_works"}
                })
            .topic("02", "What is HTTP?",
                "HTTP governs how browsers and servers communicate. Learn GET, POST, PUT, DELETE " +
                "methods, status codes (200, 404, 500), request/response headers, and the " +
                "difference between HTTP and HTTPS.",
                "be_int_http",
                new String[][]{
                    {"Official", "MDN – HTTP Overview",              "https://developer.mozilla.org/en-US/docs/Web/HTTP/Overview"},
                    {"Video",    "HTTP Crash Course – freeCodeCamp", "https://www.youtube.com/watch?v=iYM2zFP3Zn0"},
                    {"Course",   "Udacity – HTTP & Web Servers",     "https://www.udacity.com/course/http-web-servers--ud303"}
                })
            .topic("03", "What is a Domain Name?",
                "Domain names are human-readable addresses for websites. Learn about TLDs, " +
                "subdomains, DNS records (A, CNAME, MX, TXT), domain registration, WHOIS, " +
                "and how domains map to IP addresses.",
                "be_int_domain",
                new String[][]{
                    {"Official", "MDN – What is a Domain Name",          "https://developer.mozilla.org/en-US/docs/Learn/Common_questions/Web_mechanics/What_is_a_domain_name"},
                    {"Official", "ICANN – Beginner's Guide",             "https://www.icann.org/resources/pages/beginners-guides-2012-03-06-en"},
                    {"Course",   "Namecheap – DNS Learning Center",      "https://www.namecheap.com/guru-guides/"}
                })
            .topic("04", "What is Hosting?",
                "Web hosting stores your app on a server accessible via the internet. Learn " +
                "the difference between shared hosting, VPS, dedicated servers, and cloud " +
                "hosting. Understand uptime, bandwidth, and scalability tradeoffs.",
                "be_int_hosting",
                new String[][]{
                    {"Video",    "freeCodeCamp – Web Hosting Explained",  "https://www.freecodecamp.org/news/web-hosting-for-beginners/"},
                    {"Official", "DigitalOcean – Cloud Basics Tutorials", "https://www.digitalocean.com/community/tutorials"},
                    {"Official", "AWS Free Tier Docs",                   "https://aws.amazon.com/free/"}
                })
            .topic("05", "DNS and How It Works",
                "DNS translates human-readable domain names to IP addresses. Learn about " +
                "recursive resolvers, root servers, TLD servers, DNS record types, and " +
                "propagation delays. Every web request starts with a DNS lookup.",
                "be_int_dns",
                new String[][]{
                    {"Official", "Cloudflare – What is DNS?",        "https://www.cloudflare.com/learning/dns/what-is-dns/"},
                    {"Video",    "DNS Explained – freeCodeCamp",     "https://www.youtube.com/watch?v=72snZctFFtA"},
                    {"Official", "Google Public DNS Docs",           "https://developers.google.com/speed/public-dns/docs/intro"}
                })
            .topic("06", "Browsers and How They Work",
                "Understand the rendering pipeline, JavaScript engines, and how a browser " +
                "parses HTML and CSS into a DOM tree. Know the difference between the " +
                "browser environment and the server environment.",
                "be_int_browsers",
                new String[][]{
                    {"Official", "web.dev – How Browsers Work",         "https://web.dev/articles/howbrowserswork"},
                    {"Official", "MDN – Browser Engine Overview",       "https://developer.mozilla.org/en-US/docs/Glossary/Browser"},
                    {"Video",    "Google Chrome University (YouTube)",  "https://www.youtube.com/watch?v=0IsQqJ7pwhw"}
                })
            .build();
    }

    // ── 2. Frontend Basics ───────────────────────────────────────────────────

    private static BackendModule buildFrontendBasics() {
        return new Builder()
            .meta("Frontend Basics",
                  "HTML, CSS and JavaScript essentials",
                  "Frontend Basics",
                  "Even as a backend developer, understanding the frontend is critical. You need " +
                  "to know how HTML structures content, CSS styles it, and JavaScript adds " +
                  "interactivity — so you can communicate with frontend teams and debug full-stack issues.",
                  "be_fe_basics_done")
            .topic("01", "HTML",
                "HTML is the skeleton of every web page. Learn semantic elements, forms, tables, " +
                "links, and how the browser parses HTML into a DOM tree. This knowledge is key " +
                "to understanding what your backend is serving.",
                "be_fe_html",
                new String[][]{
                    {"Official", "MDN – HTML Basics",                     "https://developer.mozilla.org/en-US/docs/Learn/HTML/Introduction_to_HTML"},
                    {"Course",   "freeCodeCamp – Responsive Web Design",  "https://www.freecodecamp.org/learn/2022/responsive-web-design/"},
                    {"Course",   "W3Schools HTML Tutorial",               "https://www.w3schools.com/html/"}
                })
            .topic("02", "CSS",
                "CSS controls the visual presentation of HTML. Learn selectors, the box model, " +
                "flexbox, grid, and responsive design. As a backend developer, understanding CSS " +
                "helps you serve the right assets and debug layout-related API issues.",
                "be_fe_css",
                new String[][]{
                    {"Official", "MDN – CSS First Steps",           "https://developer.mozilla.org/en-US/docs/Learn/CSS/First_steps"},
                    {"Video",    "freeCodeCamp – CSS Full Course",  "https://www.youtube.com/watch?v=1Rs2ND1ryYc"},
                    {"Course",   "web.dev – Learn CSS",             "https://web.dev/learn/css/"}
                })
            .topic("03", "JavaScript",
                "JavaScript is widely used on the backend via Node.js. Learn variables, functions, " +
                "DOM manipulation, Promises, and async/await. Even if you use another backend " +
                "language, JS knowledge is invaluable for understanding how clients consume APIs.",
                "be_fe_js",
                new String[][]{
                    {"Course",   "The Odin Project – JavaScript",         "https://www.theodinproject.com/paths/full-stack-javascript/courses/javascript"},
                    {"Official", "javascript.info – Full JS Guide",       "https://javascript.info/"},
                    {"Course",   "freeCodeCamp – JavaScript Algorithms",  "https://www.freecodecamp.org/learn/javascript-algorithms-and-data-structures/"}
                })
            .build();
    }

    // ── 3. Pick a Backend Language ───────────────────────────────────────────

    private static BackendModule buildPickLanguage() {
        return new Builder()
            .meta("Pick a Backend Language",
                  "Choose your primary backend language",
                  "Pick a Backend Language",
                  "Your backend language is the foundation of everything you build. Each has strengths " +
                  "— Java for enterprise, Python for simplicity and ML, Node.js for full-stack, " +
                  "Go and Rust for high performance. Pick one and go deep before exploring others.",
                  "be_lang_done")
            .topic("01", "JavaScript (Node.js)",
                "Node.js lets you run JavaScript on the server. It's non-blocking and event-driven, " +
                "making it excellent for real-time apps and REST APIs. With npm you get access to " +
                "the largest package ecosystem in the world.",
                "be_lang_js",
                new String[][]{
                    {"Course",   "The Odin Project – Node.js",      "https://www.theodinproject.com/paths/full-stack-javascript"},
                    {"Course",   "freeCodeCamp – Back End APIs",    "https://www.freecodecamp.org/learn/back-end-development-and-apis/"},
                    {"Official", "Node.js Official Docs",           "https://nodejs.org/en/docs/"}
                })
            .topic("02", "Go",
                "Go (Golang) is a statically-typed, compiled language by Google for simplicity and " +
                "performance. Excellent for microservices and high-concurrency backends. Built-in " +
                "goroutines make concurrency straightforward.",
                "be_lang_go",
                new String[][]{
                    {"Official", "Go Official Tour",                    "https://go.dev/tour/welcome/1"},
                    {"Video",    "freeCodeCamp – Golang Full Course",   "https://www.youtube.com/watch?v=un6ZyFkqFKo"},
                    {"Course",   "Go by Example",                      "https://gobyexample.com/"}
                })
            .topic("03", "Python",
                "Python's clean syntax and vast ecosystem make it one of the most popular backend " +
                "languages. Use Django for full-featured apps or FastAPI/Flask for lightweight APIs. " +
                "Also dominates in data science and AI.",
                "be_lang_python",
                new String[][]{
                    {"Course",   "CS50P – Python (Harvard, Free)",   "https://cs50.harvard.edu/python/"},
                    {"Official", "Python Official Tutorial",         "https://docs.python.org/3/tutorial/"},
                    {"Video",    "Corey Schafer – Python Series",    "https://www.youtube.com/playlist?list=PL-osiE80TeTt2d9bfVyTiXJA-UTHn6WwU"}
                })
            .topic("04", "Ruby",
                "Ruby is known for elegant syntax and developer happiness. Ruby on Rails pioneered " +
                "MVC, migrations, and scaffolding conventions. Great for startups and rapid " +
                "prototyping. GitHub and Shopify were built on Rails.",
                "be_lang_ruby",
                new String[][]{
                    {"Course",   "The Odin Project – Ruby",              "https://www.theodinproject.com/paths/full-stack-ruby-on-rails"},
                    {"Official", "Ruby Official Docs",                  "https://www.ruby-lang.org/en/documentation/"},
                    {"Video",    "freeCodeCamp – Ruby on Rails",        "https://www.youtube.com/watch?v=B3Fbujmgo60"}
                })
            .topic("05", "Java",
                "Java is the king of enterprise backend development. Spring Boot makes it easy to " +
                "build production-ready REST APIs and microservices. As an Android developer you " +
                "already know Java — Spring Boot is your most natural next step.",
                "be_lang_java",
                new String[][]{
                    {"Official", "Spring Boot Official Guides",          "https://spring.io/guides"},
                    {"Video",    "Java Brains – Spring Boot (YouTube)",  "https://www.youtube.com/c/JavaBrains"},
                    {"Course",   "Baeldung – Spring Tutorials",         "https://www.baeldung.com/spring-tutorial"}
                })
            .topic("06", "C#",
                "C# with ASP.NET Core is Microsoft's powerful backend stack, widely used in " +
                "enterprise and game development. It compiles to fast native code and the .NET " +
                "ecosystem provides libraries for almost everything.",
                "be_lang_csharp",
                new String[][]{
                    {"Official", "Microsoft Learn – ASP.NET Core",   "https://learn.microsoft.com/en-us/aspnet/core/"},
                    {"Video",    "freeCodeCamp – C# Full Course",    "https://www.youtube.com/watch?v=GhQdlIFylQ8"},
                    {"Official", "dotnet Official Learn",            "https://dotnet.microsoft.com/en-us/learn"}
                })
            .topic("07", "PHP",
                "PHP powers over 75% of the web including WordPress. Laravel is its modern MVC " +
                "framework offering elegant syntax and powerful tooling. PHP is easy to deploy " +
                "and has unmatched shared-hosting support.",
                "be_lang_php",
                new String[][]{
                    {"Official", "PHP Official Manual",                "https://www.php.net/manual/en/"},
                    {"Course",   "Laracasts – Laravel from Scratch",   "https://laracasts.com/series/laravel-8-from-scratch"},
                    {"Video",    "freeCodeCamp – PHP Full Course",     "https://www.youtube.com/watch?v=OK_JCtrrv-c"}
                })
            .topic("08", "Rust",
                "Rust is a systems language focused on memory safety and blazing performance without " +
                "a garbage collector. Increasingly used for high-performance web backends and " +
                "WebAssembly. Axum and Actix-Web are popular Rust web frameworks.",
                "be_lang_rust",
                new String[][]{
                    {"Official", "The Rust Book",                         "https://doc.rust-lang.org/book/"},
                    {"Official", "Rust Official Learn Page",             "https://www.rust-lang.org/learn"},
                    {"Video",    "freeCodeCamp – Rust Beginners Course", "https://www.youtube.com/watch?v=BpPEoZW5IiY"}
                })
            .build();
    }

    // ── 4. Version Control Systems ───────────────────────────────────────────

    private static BackendModule buildVcs() {
        return new Builder()
            .meta("Version Control Systems",
                  "Git, GitHub and GitLab",
                  "Version Control Systems",
                  "Version control tracks every change to your codebase so you can collaborate, " +
                  "roll back mistakes, and ship confidently. Git is the industry standard. " +
                  "GitHub and GitLab add pull requests, code reviews, and CI/CD pipelines on top.",
                  "be_vcs_done")
            .topic("01", "Git",
                "Git is a distributed version control system. Master the core workflow: clone, add, " +
                "commit, push, pull, branch, merge, and rebase. Learn to resolve merge conflicts " +
                "and use git log to trace history. Non-negotiable for any developer.",
                "be_vcs_git",
                new String[][]{
                    {"Official", "Pro Git Book – Official",              "https://git-scm.com/book/en/v2"},
                    {"Video",    "Git & GitHub Crash Course – freeCodeCamp", "https://www.youtube.com/watch?v=RGOj5yH7evk"},
                    {"Course",   "GitHub Skills – Interactive Labs",     "https://skills.github.com/"}
                })
            .topic("02", "GitHub",
                "GitHub is the world's largest code hosting platform. Learn to create repos, fork " +
                "projects, raise pull requests, and review code. Understand GitHub Actions for " +
                "CI/CD and GitHub Issues for task tracking. Your GitHub profile is your portfolio.",
                "be_vcs_github",
                new String[][]{
                    {"Official", "GitHub Docs – Getting Started",        "https://docs.github.com/en/get-started"},
                    {"Course",   "GitHub Skills – Free Interactive",     "https://skills.github.com/"},
                    {"Video",    "freeCodeCamp – GitHub for Beginners",  "https://www.youtube.com/watch?v=tRZGeaHPoaw"}
                })
            .topic("03", "GitLab",
                "GitLab is a complete DevOps platform with built-in CI/CD, container registry, and " +
                "security scanning — and can be self-hosted for free. Learn GitLab pipelines, " +
                "merge requests, and environments. Many enterprises prefer it for its all-in-one approach.",
                "be_vcs_gitlab",
                new String[][]{
                    {"Official", "GitLab Learn – Official",           "https://about.gitlab.com/learn/"},
                    {"Video",    "GitLab CI/CD Tutorial – YouTube",   "https://www.youtube.com/watch?v=pPNBnlI_EQE"},
                    {"Course",   "Atlassian Git Tutorials",           "https://www.atlassian.com/git/tutorials"}
                })
            .build();
    }

    // ── 5. Relational Databases ──────────────────────────────────────────────

    private static BackendModule buildRelationalDB() {
        return new Builder()
            .meta("Relational Databases",
                  "SQL databases, migrations and the N+1 problem",
                  "Relational Databases",
                  "Relational databases store structured data in tables linked by foreign keys. SQL " +
                  "is the universal query language. Every backend developer must master at least one " +
                  "RDBMS. Understanding ACID, normalization, indexes, and the N+1 problem separates " +
                  "senior developers from juniors.",
                  "be_reldb_done")
            .topic("01", "MySQL",
                "The world's most widely deployed open-source RDBMS. Learn tables, primary and " +
                "foreign keys, JOINs, GROUP BY, transactions, and indexes. The InnoDB engine " +
                "supports ACID compliance and row-level locking for concurrent applications.",
                "be_reldb_mysql",
                new String[][]{
                    {"Course",   "MySQL Tutorial – W3Schools",           "https://www.w3schools.com/mysql/"},
                    {"Video",    "MySQL Crash Course – freeCodeCamp",    "https://www.youtube.com/watch?v=HXV3zeQKqGY"},
                    {"Official", "MySQL Official Docs",                  "https://dev.mysql.com/doc/"}
                })
            .topic("02", "SQLite",
                "A lightweight, serverless, file-based database embedded directly in your app — " +
                "no server process needed. You're already using it in Android via Room. Perfect " +
                "for mobile apps, prototyping, and desktop applications.",
                "be_reldb_sqlite",
                new String[][]{
                    {"Official", "SQLite Official Docs",         "https://www.sqlite.org/docs.html"},
                    {"Course",   "SQLite Tutorial.net",          "https://www.sqlitetutorial.net/"},
                    {"Course",   "CS50 SQL – Harvard (Free)",    "https://cs50.harvard.edu/sql/"}
                })
            .topic("03", "Oracle",
                "Oracle Database is the dominant enterprise-grade RDBMS used in banks and government " +
                "systems. It offers advanced features like partitioning, RAC clusters, and PL/SQL. " +
                "Understanding Oracle opens doors to high-paying enterprise backend roles.",
                "be_reldb_oracle",
                new String[][]{
                    {"Official", "Oracle Live SQL – Free Practice",      "https://livesql.oracle.com/"},
                    {"Official", "Oracle Database Concepts Docs",        "https://docs.oracle.com/en/database/oracle/oracle-database/"},
                    {"Video",    "freeCodeCamp – Oracle SQL Tutorial",   "https://www.youtube.com/watch?v=-TP_qRRUado"}
                })
            .topic("04", "MS SQL Server",
                "Widely used in enterprises running the .NET stack. Features T-SQL, SQL Server " +
                "Agent for scheduling, and SSMS for GUI management. Learn stored procedures, views, " +
                "triggers, and query execution plans.",
                "be_reldb_mssql",
                new String[][]{
                    {"Official", "Microsoft Learn – SQL Server",         "https://learn.microsoft.com/en-us/sql/sql-server/"},
                    {"Course",   "W3Schools – SQL Tutorial",             "https://www.w3schools.com/sql/"},
                    {"Video",    "freeCodeCamp – SQL Server Course",     "https://www.youtube.com/watch?v=7GVFYt6_ZFM"}
                })
            .topic("05", "MariaDB",
                "A community-driven fork of MySQL by its original developers, offering improved " +
                "performance, more storage engines, and enhanced security. A drop-in MySQL " +
                "replacement popular in Linux LAMP stacks and many cloud providers.",
                "be_reldb_mariadb",
                new String[][]{
                    {"Official", "MariaDB Official Docs",            "https://mariadb.com/kb/en/documentation/"},
                    {"Official", "MariaDB Knowledgebase",            "https://mariadb.com/kb/en/"},
                    {"Course",   "W3Schools – MySQL/MariaDB SQL",    "https://www.w3schools.com/mysql/"}
                })
            .topic("06", "PostgreSQL",
                "The most powerful open-source RDBMS. Supports advanced data types (JSON, arrays, " +
                "UUID), full-text search, window functions, and custom extensions. Follows the SQL " +
                "standard strictly. Preferred for complex queries and strict data integrity.",
                "be_reldb_postgres",
                new String[][]{
                    {"Course",   "PostgreSQL Tutorial",         "https://www.postgresqltutorial.com/"},
                    {"Course",   "CS50 SQL – Harvard (Free)",   "https://cs50.harvard.edu/sql/"},
                    {"Official", "SQLZoo – Interactive SQL",    "https://sqlzoo.net/"}
                })
            .topic("07", "Migrations",
                "Database migrations are version-controlled scripts that evolve your schema over " +
                "time without losing data — adding columns, renaming tables, creating indexes. " +
                "Tools: Flyway (Java), Liquibase, Alembic (Python). Critical for team collaboration " +
                "and production deployments.",
                "be_reldb_migrations",
                new String[][]{
                    {"Official", "Flyway – Getting Started",          "https://documentation.red-gate.com/fd/getting-started-with-flyway-184127223.html"},
                    {"Official", "Liquibase Quickstart",              "https://www.liquibase.org/get-started/quickstart"},
                    {"Official", "Alembic Docs – SQLAlchemy",        "https://alembic.sqlalchemy.org/en/latest/tutorial.html"}
                })
            .topic("08", "N+1 Problem",
                "A common performance killer where code issues 1 query to fetch a list, then N " +
                "additional queries for related data on each row — instead of using a JOIN. " +
                "Learn to identify it in ORM logs and fix it with eager loading or batch queries.",
                "be_reldb_nplusone",
                new String[][]{
                    {"Course",   "Baeldung – Hibernate N+1 Problem",      "https://www.baeldung.com/hibernate-common-performance-problems-in-logs"},
                    {"Official", "Hibernate – Eager vs Lazy Loading",     "https://docs.jboss.org/hibernate/orm/current/userguide/html_single/Hibernate_User_Guide.html#fetching"},
                    {"Official", "SQLAlchemy – Eager Loading Docs",       "https://docs.sqlalchemy.org/en/20/orm/loading_relationships.html"}
                })
            .build();
    }

    // ── 6. APIs ──────────────────────────────────────────────────────────────

    private static BackendModule buildApis() {
        return new Builder()
            .meta("Learn about APIs",
                  "REST, GraphQL, Auth, Security & Best Practices",
                  "APIs (Application Programming Interfaces)",
                  "APIs are how your backend communicates with the outside world — browsers, " +
                  "mobile apps, and other services. Master multiple API styles, authentication " +
                  "strategies, OpenAPI documentation, and security best practices.",
                  "be_apis_done")
            // API Styles
            .topic("01", "REST",
                "The most popular API style. Uses HTTP methods (GET, POST, PUT, PATCH, DELETE) " +
                "and stateless requests. Resources identified by URLs, data exchanged as JSON. " +
                "Master resource naming, status codes, pagination, and versioning.",
                "be_api_rest",
                new String[][]{
                    {"Course",   "RESTful API Design – Codecademy",       "https://www.codecademy.com/article/what-is-rest"},
                    {"Video",    "REST API Crash Course – Traversy Media", "https://www.youtube.com/watch?v=Q-BpqyOT3a8"},
                    {"Course",   "freeCodeCamp – REST API Tutorial",      "https://www.freecodecamp.org/news/rest-api-tutorial-rest-client-rest-service-and-api-calls-explained-with-code-examples/"}
                })
            .topic("02", "JSON APIs",
                "JSON is the universal data format for web APIs. Learn to serialize and deserialize " +
                "JSON, handle nested objects, arrays, null values, and data types. The JSON:API " +
                "specification provides conventions for consistent request/response structures.",
                "be_api_json",
                new String[][]{
                    {"Official", "JSON.org – Official Introduction",  "https://www.json.org/json-en.html"},
                    {"Official", "MDN – Working with JSON",           "https://developer.mozilla.org/en-US/docs/Learn/JavaScript/Objects/JSON"},
                    {"Course",   "JSONPlaceholder – Practice API",    "https://jsonplaceholder.typicode.com/"}
                })
            .topic("03", "SOAP",
                "An XML-based protocol used heavily in enterprise and banking systems. Defines a " +
                "strict message format using WSDL. While REST has largely replaced it in new " +
                "projects, many legacy systems still expose SOAP endpoints.",
                "be_api_soap",
                new String[][]{
                    {"Course",   "W3Schools – SOAP Tutorial",      "https://www.w3schools.com/xml/xml_soap.asp"},
                    {"Official", "IBM – What is SOAP?",            "https://www.ibm.com/think/topics/soap-vs-rest"},
                    {"Official", "MDN – SOAP Web Services",        "https://developer.mozilla.org/en-US/docs/Glossary/SOAP"}
                })
            .topic("04", "gRPC",
                "A high-performance RPC framework from Google using Protocol Buffers (protobuf) " +
                "for efficient binary serialization — much faster than JSON. Excellent for " +
                "microservice-to-microservice communication. Supports streaming and is language-agnostic.",
                "be_api_grpc",
                new String[][]{
                    {"Official", "gRPC Official Docs",                   "https://grpc.io/docs/"},
                    {"Video",    "gRPC Crash Course – freeCodeCamp",     "https://www.youtube.com/watch?v=Yw4rkaTc0f8"},
                    {"Official", "Protocol Buffers Docs",                "https://protobuf.dev/"}
                })
            .topic("05", "GraphQL",
                "A query language for APIs by Facebook where the client specifies exactly what " +
                "data it needs, eliminating over-fetching and under-fetching. A single endpoint " +
                "handles all queries, mutations, and subscriptions. Apollo and Hasura are popular tools.",
                "be_api_graphql",
                new String[][]{
                    {"Official", "GraphQL Official Learn",           "https://graphql.org/learn/"},
                    {"Course",   "How to GraphQL – Full Tutorial",   "https://www.howtographql.com/"},
                    {"Official", "Apollo GraphQL Docs",              "https://www.apollographql.com/docs/"}
                })
            // OpenAPI
            .topic("06", "Open API Specs",
                "The OpenAPI Specification (formerly Swagger) is the standard for documenting REST " +
                "APIs. Write a YAML/JSON spec and tools auto-generate interactive docs, client SDKs, " +
                "and server stubs. Makes your API self-documenting.",
                "be_api_openapi",
                new String[][]{
                    {"Official", "Swagger Official Docs",         "https://swagger.io/docs/"},
                    {"Official", "OpenAPI 3.0 Guide",             "https://swagger.io/specification/"},
                    {"Course",   "Postman API Documentation",     "https://learning.postman.com/docs/publishing-your-api/documenting-your-api/"}
                })
            // Authentication
            .topic("07", "JWT",
                "JWT is a compact, self-contained token for stateless authentication. After login, " +
                "the server issues a signed JWT the client sends in every request header. Learn the " +
                "header.payload.signature structure, expiry (exp), and refresh token patterns.",
                "be_api_jwt",
                new String[][]{
                    {"Official", "JWT.io – Introduction",               "https://jwt.io/introduction"},
                    {"Course",   "Auth0 – JWT Handbook",                "https://auth0.com/resources/ebooks/jwt-handbook"},
                    {"Video",    "freeCodeCamp – JWT Explained",        "https://www.freecodecamp.org/news/what-are-json-web-tokens-jwt-auth-tutorial/"}
                })
            .topic("08", "OAuth",
                "OAuth 2.0 is the standard for authorization delegation — it powers 'Sign in with " +
                "Google' and 'Login with GitHub'. Learn authorization code flow, PKCE, scopes, and " +
                "access vs refresh tokens. You're using OAuth via Firebase in your Android app.",
                "be_api_oauth",
                new String[][]{
                    {"Course",   "OAuth 2.0 Simplified – Aaron Parecki",  "https://www.oauth.com/"},
                    {"Official", "Auth0 – OAuth Overview",               "https://auth0.com/intro-to-iam/what-is-oauth-2"},
                    {"Official", "Google Identity – OAuth 2.0",          "https://developers.google.com/identity/protocols/oauth2"}
                })
            .topic("09", "Basic Authentication",
                "HTTP Basic Auth sends credentials as a Base64-encoded username:password in the " +
                "Authorization header. Simple to implement but always requires HTTPS. Suitable " +
                "for internal tools and simple API clients.",
                "be_api_basic_auth",
                new String[][]{
                    {"Official", "MDN – HTTP Authentication",        "https://developer.mozilla.org/en-US/docs/Web/HTTP/Authentication"},
                    {"Official", "RFC 7617 – Basic Auth Spec",       "https://www.rfc-editor.org/rfc/rfc7617"},
                    {"Course",   "Postman – Basic Auth Guide",       "https://learning.postman.com/docs/sending-requests/authorization/"}
                })
            .topic("10", "Token Authentication",
                "Token-based auth stores a random opaque token in a database and checks it on each " +
                "request. Tokens can be revoked instantly unlike JWTs. Used by Django REST Framework, " +
                "GitHub Personal Access Tokens, and API keys.",
                "be_api_token_auth",
                new String[][]{
                    {"Official", "Django REST – Token Auth Docs",     "https://www.django-rest-framework.org/api-guide/authentication/#tokenauthentication"},
                    {"Official", "Auth0 – Token-Based Auth Guide",    "https://auth0.com/learn/token-based-authentication-made-easy"},
                    {"Video",    "freeCodeCamp – API Key Authentication", "https://www.freecodecamp.org/news/how-to-create-an-api-key/"}
                })
            .topic("11", "Cookie Based Auth",
                "Cookie-based (session) authentication stores a session ID in an HTTP cookie after " +
                "login. Learn HttpOnly and Secure cookie flags, SameSite CSRF protection, and " +
                "session fixation attacks. This is the traditional web app authentication model.",
                "be_api_cookie_auth",
                new String[][]{
                    {"Official", "MDN – HTTP Cookies",                    "https://developer.mozilla.org/en-US/docs/Web/HTTP/Cookies"},
                    {"Official", "OWASP – Session Management Cheat Sheet","https://cheatsheetseries.owasp.org/cheatsheets/Session_Management_Cheat_Sheet.html"},
                    {"Course",   "web.dev – SameSite Cookies Explained",  "https://web.dev/articles/samesite-cookies-explained"}
                })
            .topic("12", "OpenID",
                "OpenID Connect (OIDC) is an identity layer on top of OAuth 2.0. While OAuth handles " +
                "'what you can access', OIDC handles 'who you are'. It provides an ID Token (JWT) " +
                "with user identity claims. Firebase Authentication uses OIDC under the hood.",
                "be_api_openid",
                new String[][]{
                    {"Official", "OpenID Connect Official",               "https://openid.net/developers/how-connect-works/"},
                    {"Official", "Auth0 – OIDC Overview",                "https://auth0.com/docs/authenticate/protocols/openid-connect-protocol"},
                    {"Course",   "Okta – OIDC & OAuth 2.0 Guide",        "https://developer.okta.com/docs/concepts/oauth-openid/"}
                })
            .topic("13", "SAML",
                "SAML is an XML-based standard for enterprise SSO (Single Sign-On). Lets employees " +
                "log in once to their company's identity provider and access multiple services. " +
                "Common in large organizations and SaaS B2B products.",
                "be_api_saml",
                new String[][]{
                    {"Official", "SAML.xml – Official Overview",   "https://saml.xml.org/saml-specifications"},
                    {"Official", "Okta – What is SAML?",           "https://www.okta.com/blog/2020/09/what-is-saml/"},
                    {"Course",   "OneLogin – SAML Tutorial",       "https://developers.onelogin.com/saml"}
                })
            // Web Security / Hashing
            .topic("14", "MD5",
                "MD5 produces a 128-bit hash once used for password hashing. It is now considered " +
                "cryptographically broken — NEVER use it for passwords. Understand why it's " +
                "vulnerable to collision and rainbow table attacks, and know what to use instead.",
                "be_api_md5",
                new String[][]{
                    {"Official", "Cloudflare – What is MD5?",           "https://www.cloudflare.com/learning/ssl/what-is-hashing/"},
                    {"Official", "OWASP – Password Storage Cheat Sheet","https://cheatsheetseries.owasp.org/cheatsheets/Password_Storage_Cheat_Sheet.html"},
                    {"Video",    "freeCodeCamp – Hashing Explained",    "https://www.freecodecamp.org/news/md5-vs-sha-1-vs-sha-2-which-is-the-most-secure-encryption-hash/"}
                })
            .topic("15", "SHA",
                "SHA family — SHA-256 and SHA-512 — are currently secure and used for data integrity " +
                "priority(code signing, SSL certificates, blockchain). Plain SHA is too fast for password " +
                "hashing — use bcrypt or Argon2 for passwords instead.",
                "be_api_sha",
                new String[][]{
                    {"Official", "NIST – Hash Functions Overview",    "https://csrc.nist.gov/projects/hash-functions"},
                    {"Video",    "Computerphile – SHA Explained",     "https://www.youtube.com/watch?v=DMtFhACPnTY"},
                    {"Course",   "SSL.com – Hashing Algorithms",      "https://www.ssl.com/article/what-are-sha-1-sha-2-and-sha-256-how-do-they-work/"}
                })
            .topic("16", "scrypt",
                "scrypt is a memory-hard password-based key derivation function that makes brute-force " +
                "attacks expensive in both CPU and RAM. Designed to resist hardware attacks like ASIC " +
                "crackers. Recommended for high-security password storage alongside Argon2.",
                "be_api_scrypt",
                new String[][]{
                    {"Official", "scrypt – Original Paper",              "https://www.tarsnap.com/scrypt.html"},
                    {"Official", "OWASP – Password Storage: scrypt",    "https://cheatsheetseries.owasp.org/cheatsheets/Password_Storage_Cheat_Sheet.html"},
                    {"Course",   "Authlib – scrypt Docs",               "https://docs.authlib.org/en/latest/"}
                })
            .topic("17", "bcrypt",
                "The most widely used adaptive password hashing algorithm. Automatically salts " +
                "passwords (preventing rainbow table attacks) and has a configurable cost factor " +
                "so you can make it slower as hardware gets faster. Never store plain-text passwords.",
                "be_api_bcrypt",
                new String[][]{
                    {"Official", "bcrypt – Algorithm Overview",           "https://en.wikipedia.org/wiki/Bcrypt"},
                    {"Official", "OWASP – Password Storage Cheat Sheet", "https://cheatsheetseries.owasp.org/cheatsheets/Password_Storage_Cheat_Sheet.html"},
                    {"Course",   "Auth0 – Hashing in Action with bcrypt","https://auth0.com/blog/hashing-in-action-understanding-bcrypt/"}
                })
            // HTTPS / Web Security
            .topic("18", "HTTPS",
                "HTTPS encrypts all data between client and server using TLS. Learn how SSL " +
                "certificates work, certificate authorities, the TLS handshake, and how to get a " +
                "free certificate via Let's Encrypt. All production backends must use HTTPS.",
                "be_api_https",
                new String[][]{
                    {"Official", "Let's Encrypt – How HTTPS Works",  "https://letsencrypt.org/how-it-works/"},
                    {"Official", "Cloudflare – What is TLS?",        "https://www.cloudflare.com/learning/ssl/transport-layer-security-tls/"},
                    {"Course",   "SSL.com – TLS Handshake Explained","https://www.ssl.com/article/ssl-tls-handshake-overview/"}
                })
            .topic("19", "OWASP Risks",
                "OWASP Top 10 lists the most critical web security risks: SQL Injection, Broken " +
                "Authentication, XSS, Insecure Deserialization, Security Misconfiguration, and more. " +
                "Every backend developer must know these attack vectors and how to defend against each.",
                "be_api_owasp",
                new String[][]{
                    {"Official", "OWASP Top 10 – Official",              "https://owasp.org/www-project-top-ten/"},
                    {"Course",   "PortSwigger Web Security Academy",     "https://portswigger.net/web-security"},
                    {"Official", "OWASP WebGoat – Practice Lab",        "https://owasp.org/www-project-webgoat/"}
                })
            .topic("20", "CORS",
                "CORS controls which origins can make requests to your API. Learn how to configure " +
                "Access-Control-Allow-Origin, preflight OPTIONS requests, credentials mode, and " +
                "wildcard vs specific origins. Misconfiguring CORS is a common vulnerability.",
                "be_api_cors",
                new String[][]{
                    {"Official", "MDN – CORS Explained",                  "https://developer.mozilla.org/en-US/docs/Web/HTTP/CORS"},
                    {"Official", "web.dev – Cross-Origin Resource Sharing","https://web.dev/articles/cross-origin-resource-sharing"},
                    {"Course",   "freeCodeCamp – CORS Tutorial",         "https://www.freecodecamp.org/news/cors-explained/"}
                })
            .topic("21", "SSL/TLS",
                "SSL and its successor TLS are cryptographic protocols for secure communication. " +
                "Understand symmetric vs asymmetric encryption, certificate chains, SNI, TLS 1.2 vs " +
                "1.3 improvements, and how to configure your server's TLS settings correctly.",
                "be_api_ssl",
                new String[][]{
                    {"Official", "Cloudflare – SSL/TLS Learning Center",  "https://www.cloudflare.com/learning/ssl/what-is-ssl/"},
                    {"Course",   "SSL.com – SSL vs TLS",                 "https://www.ssl.com/article/ssl-vs-tls-whats-the-difference/"},
                    {"Official", "Mozilla SSL Config Generator",         "https://ssl-config.mozilla.org/"}
                })
            .topic("22", "CSP",
                "Content Security Policy is an HTTP response header that tells browsers which " +
                "content sources are allowed, dramatically reducing XSS attack surface. Learn " +
                "directives like script-src, style-src, img-src, and use report-uri to collect " +
                "violations before enforcing.",
                "be_api_csp",
                new String[][]{
                    {"Official", "MDN – Content Security Policy",    "https://developer.mozilla.org/en-US/docs/Web/HTTP/CSP"},
                    {"Official", "web.dev – CSP Guide",              "https://web.dev/articles/csp"},
                    {"Course",   "CSP Evaluator – Google Tool",      "https://csp-evaluator.withgoogle.com/"}
                })
            .topic("23", "Server Security",
                "Securing your server: keep OS and software updated, disable unused ports, configure " +
                "firewalls (ufw, iptables), use SSH keys instead of passwords, run apps as non-root " +
                "users, and monitor logs for intrusion attempts.",
                "be_api_server_sec",
                new String[][]{
                    {"Course",   "DigitalOcean – Server Security Guide",   "https://www.digitalocean.com/community/tutorials/an-introduction-to-securing-your-linux-vps"},
                    {"Official", "OWASP – Infrastructure Security",       "https://owasp.org/www-project-web-security-testing-guide/"},
                    {"Course",   "Linux Foundation – Security Essentials","https://training.linuxfoundation.org/training/fundamentals-of-linux-security/"}
                })
            .topic("24", "API Security Best Practices",
                "Secure your APIs: validate and sanitize all input, use HTTPS everywhere, implement " +
                "rate limiting, set proper CORS policies, use parameterized queries to prevent SQL " +
                "injection, log API calls, rotate secrets regularly, never expose stack traces to clients.",
                "be_api_best_practices",
                new String[][]{
                    {"Official", "OWASP API Security Top 10",          "https://owasp.org/www-project-api-security/"},
                    {"Course",   "Postman – API Security Guide",       "https://www.postman.com/api-platform/api-security/"},
                    {"Official", "Auth0 – Secure API Best Practices",  "https://auth0.com/blog/nine-tips-to-harden-your-security-practices/"}
                })
            .build();
    }

    // ── 7. Caching ───────────────────────────────────────────────────────────

    private static BackendModule buildCaching() {
        return new Builder()
            .meta("Caching",
                  "Redis, Memcached and HTTP Caching",
                  "Caching",
                  "Caching stores frequently accessed data in fast temporary storage so your " +
                  "backend doesn't hit the database on every request. It dramatically reduces " +
                  "latency and database load. Learn server-side caches (Redis, Memcached) and " +
                  "HTTP-level caching headers.",
                  "be_caching_done")
            .topic("01", "Redis",
                "Redis is an in-memory key-value store for caching, session management, and pub/sub " +
                "messaging. Learn basic commands (SET, GET, EXPIRE), data structures (strings, hashes, " +
                "sorted sets), TTL (time-to-live), and cache invalidation strategies.",
                "be_cache_redis",
                new String[][]{
                    {"Course",   "Redis University – Free Courses",       "https://university.redis.com/"},
                    {"Video",    "Redis Crash Course – freeCodeCamp",     "https://www.youtube.com/watch?v=jgpVdJB2sKQ"},
                    {"Official", "Try Redis – Interactive Browser Tutorial","https://try.redis.io/"}
                })
            .topic("02", "Memcached",
                "Memcached is a simple, high-performance distributed memory cache. Unlike Redis it " +
                "only supports strings and has no persistence or pub/sub. It's multi-threaded so it " +
                "can outperform Redis for simple caching workloads on multi-core servers.",
                "be_cache_memcached",
                new String[][]{
                    {"Official", "Memcached Official Wiki",              "https://github.com/memcached/memcached/wiki"},
                    {"Course",   "DigitalOcean – Memcached Setup Guide", "https://www.digitalocean.com/community/tutorials/how-to-install-and-secure-memcached-on-ubuntu-20-04"},
                    {"Video",    "Memcached vs Redis – YouTube",         "https://www.youtube.com/watch?v=wu8xmTsFGJ0"}
                })
            .topic("03", "HTTP Caching",
                "HTTP caching uses response headers to store data at the browser, CDN, or proxy level. " +
                "Learn Cache-Control directives (max-age, no-cache, no-store), ETags for conditional " +
                "requests, and Last-Modified headers. Correct HTTP caching can eliminate server " +
                "requests entirely.",
                "be_cache_http",
                new String[][]{
                    {"Official", "MDN – HTTP Caching",                      "https://developer.mozilla.org/en-US/docs/Web/HTTP/Caching"},
                    {"Official", "web.dev – HTTP Cache (Google)",           "https://web.dev/articles/http-cache"},
                    {"Official", "Cloudflare – What is Caching?",          "https://www.cloudflare.com/learning/cdn/what-is-caching/"}
                })
            .build();
    }

    // ── 8. Web Servers ───────────────────────────────────────────────────────

    private static BackendModule buildWebServers() {
        return new Builder()
            .meta("Learn about Web Servers",
                  "Nginx, Apache, Caddy and MS IIS",
                  "Web Servers",
                  "A web server handles incoming HTTP requests and serves responses. It's the " +
                  "gateway between clients and your backend application. Learn the major web servers, " +
                  "how to configure them as reverse proxies, enable HTTPS, and serve static files " +
                  "efficiently.",
                  "be_webserver_done")
            .topic("01", "Nginx",
                "Nginx is a high-performance, event-driven web server, reverse proxy, and load " +
                "balancer. Learn how to configure server blocks (virtual hosts), proxy requests to " +
                "your app server, enable HTTPS with Let's Encrypt, serve static files, and configure " +
                "gzip compression.",
                "be_ws_nginx",
                new String[][]{
                    {"Official", "Nginx Official Beginner's Guide",       "https://nginx.org/en/docs/beginners_guide.html"},
                    {"Video",    "Nginx Crash Course – Traversy Media",   "https://www.youtube.com/watch?v=7VAI73roXaY"},
                    {"Course",   "DigitalOcean – Nginx Tutorials",        "https://www.digitalocean.com/community/tags/nginx"}
                })
            .topic("02", "Apache",
                "Apache HTTP Server is one of the oldest and most widely used web servers. Learn " +
                "about virtual hosts, .htaccess files, mod_rewrite for URL routing, and Apache " +
                "modules. Understand how it differs from Nginx — process-based vs event-driven.",
                "be_ws_apache",
                new String[][]{
                    {"Official", "Apache Official Documentation",          "https://httpd.apache.org/docs/"},
                    {"Course",   "DigitalOcean – Apache vs Nginx",        "https://www.digitalocean.com/community/tutorials/apache-vs-nginx-practical-considerations"},
                    {"Official", "Apache Getting Started",                "https://httpd.apache.org/docs/current/getting-started.html"}
                })
            .topic("03", "Caddy",
                "Caddy is a modern, easy-to-use web server that automatically obtains and renews " +
                "HTTPS certificates via Let's Encrypt — no manual configuration needed. Its Caddyfile " +
                "syntax is far simpler than Nginx config. Great for developers who want HTTPS out " +
                "of the box with minimal setup.",
                "be_ws_caddy",
                new String[][]{
                    {"Official", "Caddy Official Docs",                    "https://caddyserver.com/docs/"},
                    {"Video",    "Caddy Web Server Tutorial – YouTube",    "https://www.youtube.com/watch?v=t4naLFSlBpQ"},
                    {"Course",   "DigitalOcean – Getting Started with Caddy","https://www.digitalocean.com/community/tutorials/how-to-host-a-website-with-caddy-on-ubuntu-22-04"}
                })
            .topic("04", "MS IIS",
                "Internet Information Services (IIS) is Microsoft's web server for Windows, tightly " +
                "integrated with the .NET/ASP.NET ecosystem. Learn to configure sites, application " +
                "pools, bindings, SSL certificates, URL rewriting, and IIS Manager. Essential " +
                "knowledge for enterprise Windows-based deployments.",
                "be_ws_iis",
                new String[][]{
                    {"Official", "Microsoft Learn – IIS Documentation",   "https://learn.microsoft.com/en-us/iis/"},
                    {"Video",    "IIS Tutorial – freeCodeCamp YouTube",   "https://www.youtube.com/watch?v=1sHT0psFonM"},
                    {"Course",   "IIS.net – Getting Started",             "https://www.iis.net/learn/get-started"}
                })
            .build();
    }
}
