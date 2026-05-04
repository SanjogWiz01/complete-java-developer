from reportlab.lib.pagesizes import letter, A4
from reportlab.lib import colors
from reportlab.lib.styles import getSampleStyleSheet, ParagraphStyle
from reportlab.lib.units import inch
from reportlab.platypus import SimpleDocTemplate, Table, TableStyle, Paragraph, Spacer, PageBreak
from reportlab.platypus import ListFlowable, ListItem
from reportlab.lib.enums import TA_CENTER, TA_LEFT, TA_JUSTIFY
from datetime import datetime

# Create PDF
doc = SimpleDocTemplate("/mnt/user-data/outputs/Technology_Curriculum_Syllabus.pdf", 
                       pagesize=letter,
                       rightMargin=72, leftMargin=72,
                       topMargin=72, bottomMargin=18)

# Container for the 'Flowable' objects
elements = []

# Define styles
styles = getSampleStyleSheet()

# Custom styles
title_style = ParagraphStyle(
    'CustomTitle',
    parent=styles['Heading1'],
    fontSize=24,
    textColor=colors.HexColor('#1a237e'),
    spaceAfter=30,
    alignment=TA_CENTER,
    fontName='Helvetica-Bold'
)

heading1_style = ParagraphStyle(
    'CustomHeading1',
    parent=styles['Heading1'],
    fontSize=18,
    textColor=colors.HexColor('#283593'),
    spaceAfter=12,
    spaceBefore=12,
    fontName='Helvetica-Bold'
)

heading2_style = ParagraphStyle(
    'CustomHeading2',
    parent=styles['Heading2'],
    fontSize=14,
    textColor=colors.HexColor('#3949ab'),
    spaceAfter=10,
    spaceBefore=10,
    fontName='Helvetica-Bold'
)

heading3_style = ParagraphStyle(
    'CustomHeading3',
    parent=styles['Heading3'],
    fontSize=12,
    textColor=colors.HexColor('#5c6bc0'),
    spaceAfter=8,
    spaceBefore=8,
    fontName='Helvetica-Bold'
)

normal_style = ParagraphStyle(
    'CustomNormal',
    parent=styles['Normal'],
    fontSize=11,
    spaceAfter=6,
    alignment=TA_JUSTIFY
)

# Title Page
elements.append(Spacer(1, 2*inch))
elements.append(Paragraph("TECHNOLOGY CURRICULUM SYLLABUS", title_style))
elements.append(Spacer(1, 0.3*inch))
elements.append(Paragraph("Cybersecurity, Cloud Computing, Data Analysis & Networking", heading2_style))
elements.append(Spacer(1, 0.2*inch))
elements.append(Paragraph("Classes 1-9", heading2_style))
elements.append(Spacer(1, 0.5*inch))

# Course Overview Table
course_info = [
    ['Duration:', '1 Academic Year (7 Chapters)'],
    ['Target Audience:', 'Students Classes 1-9'],
    ['Prerequisites:', 'Basic computer literacy'],
    ['Format:', 'Hands-on practical & theoretical learning'],
]

t = Table(course_info, colWidths=[2*inch, 4*inch])
t.setStyle(TableStyle([
    ('BACKGROUND', (0, 0), (0, -1), colors.HexColor('#e8eaf6')),
    ('TEXTCOLOR', (0, 0), (-1, -1), colors.black),
    ('ALIGN', (0, 0), (-1, -1), 'LEFT'),
    ('FONTNAME', (0, 0), (0, -1), 'Helvetica-Bold'),
    ('FONTSIZE', (0, 0), (-1, -1), 11),
    ('GRID', (0, 0), (-1, -1), 1, colors.HexColor('#9fa8da')),
    ('VALIGN', (0, 0), (-1, -1), 'MIDDLE'),
    ('LEFTPADDING', (0, 0), (-1, -1), 12),
    ('RIGHTPADDING', (0, 0), (-1, -1), 12),
    ('TOPPADDING', (0, 0), (-1, -1), 10),
    ('BOTTOMPADDING', (0, 0), (-1, -1), 10),
]))
elements.append(t)
elements.append(Spacer(1, 0.3*inch))

# Learning Objectives
elements.append(Paragraph("Course Learning Objectives", heading2_style))
objectives = [
    "Develop foundational understanding of modern technology concepts",
    "Build practical programming skills using Python",
    "Understand cloud computing platforms and their applications",
    "Master networking fundamentals and protocols",
    "Learn cybersecurity best practices and threat prevention",
    "Explore data analysis techniques and visualization",
    "Introduction to Java, JavaScript, and Data Structures"
]

for obj in objectives:
    elements.append(Paragraph(f"• {obj}", normal_style))

elements.append(PageBreak())

# Table of Contents
elements.append(Paragraph("TABLE OF CONTENTS", heading1_style))
elements.append(Spacer(1, 0.2*inch))

toc_data = [
    ['Chapter 1:', 'Foundations of Technology & Programming', '4'],
    ['Chapter 2:', 'Python Programming Fundamentals', '5'],
    ['Chapter 3:', 'Cloud Computing Essentials', '7'],
    ['Chapter 4:', 'Networking Fundamentals & Protocols', '9'],
    ['Chapter 5:', 'Cybersecurity & Threat Protection', '11'],
    ['Chapter 6:', 'Data Structures & Algorithms Basics', '13'],
    ['Chapter 7:', 'Advanced Topics & Revision', '14'],
]

toc_table = Table(toc_data, colWidths=[1*inch, 4.5*inch, 0.5*inch])
toc_table.setStyle(TableStyle([
    ('BACKGROUND', (0, 0), (-1, -1), colors.HexColor('#f5f5f5')),
    ('TEXTCOLOR', (0, 0), (-1, -1), colors.black),
    ('ALIGN', (0, 0), (0, -1), 'LEFT'),
    ('ALIGN', (2, 0), (2, -1), 'RIGHT'),
    ('FONTNAME', (0, 0), (0, -1), 'Helvetica-Bold'),
    ('FONTSIZE', (0, 0), (-1, -1), 11),
    ('GRID', (0, 0), (-1, -1), 1, colors.grey),
    ('LEFTPADDING', (0, 0), (-1, -1), 10),
    ('RIGHTPADDING', (0, 0), (-1, -1), 10),
    ('TOPPADDING', (0, 0), (-1, -1), 8),
    ('BOTTOMPADDING', (0, 0), (-1, -1), 8),
]))
elements.append(toc_table)
elements.append(PageBreak())

# CHAPTER 1: Foundations of Technology & Programming
elements.append(Paragraph("CHAPTER 1", heading1_style))
elements.append(Paragraph("Foundations of Technology & Programming", heading1_style))
elements.append(Spacer(1, 0.2*inch))

elements.append(Paragraph("1.1 Introduction to Computing & Technology", heading2_style))
elements.append(Paragraph("""
This introductory module establishes the foundation for understanding modern technology. 
Students will explore how computers work, the evolution of technology, and its impact on 
daily life and society.
""", normal_style))

topics_1_1 = [
    "What is a Computer? - Hardware components (CPU, RAM, Storage, Input/Output devices)",
    "Brief history of computers - From ENIAC to modern smartphones",
    "Digital vs. Analog technology",
    "Binary number system - Understanding 0s and 1s",
    "How data is stored and processed",
    "Career opportunities in technology"
]
for topic in topics_1_1:
    elements.append(Paragraph(f"• {topic}", normal_style))

elements.append(Spacer(1, 0.15*inch))
elements.append(Paragraph("1.2 Introduction to Artificial Intelligence", heading2_style))
elements.append(Paragraph("""
Understanding AI helps students grasp how intelligent systems work and their applications 
in everyday technology like virtual assistants, recommendation systems, and autonomous vehicles.
""", normal_style))

topics_1_2 = [
    "What is Artificial Intelligence? - Definition and scope",
    "Types of AI: Narrow AI vs. General AI",
    "Machine Learning basics - How computers learn from data",
    "Real-world AI applications: Siri/Alexa, YouTube recommendations, Face recognition",
    "Ethics in AI - Bias, privacy, and responsible AI use",
    "Future of AI in education, healthcare, and transportation"
]
for topic in topics_1_2:
    elements.append(Paragraph(f"• {topic}", normal_style))

elements.append(Spacer(1, 0.15*inch))
elements.append(Paragraph("1.3 What is Programming?", heading2_style))
elements.append(Paragraph("""
Programming is the process of creating instructions for computers. This section introduces 
students to the fundamental concepts of coding and computational thinking.
""", normal_style))

topics_1_3 = [
    "Introduction to programming languages (Python, Java, JavaScript, C++)",
    "How programs work - Input, Processing, Output",
    "Algorithms and flowcharts - Visual representation of logic",
    "Understanding syntax and semantics",
    "Debugging - Finding and fixing errors",
    "First simple program: 'Hello World!' in Python"
]
for topic in topics_1_3:
    elements.append(Paragraph(f"• {topic}", normal_style))

elements.append(Spacer(1, 0.15*inch))
elements.append(Paragraph("1.4 Development Environments & Tools", heading2_style))
elements.append(Paragraph("""
Modern programmers use specialized tools to write, test, and debug code efficiently. 
This section introduces students to professional development environments.
""", normal_style))

topics_1_4 = [
    "What is an IDE (Integrated Development Environment)?",
    "Introduction to VS Code - Installation and setup",
    "VS Code interface: Editor, File Explorer, Terminal, Extensions",
    "Code syntax highlighting and autocomplete features",
    "Installing Python extension for VS Code",
    "Creating and running your first Python file in VS Code",
    "VS Code shortcuts for productivity"
]
for topic in topics_1_4:
    elements.append(Paragraph(f"• {topic}", normal_style))

elements.append(Spacer(1, 0.15*inch))
elements.append(Paragraph("1.5 Command Line & Terminal Basics", heading2_style))
elements.append(Paragraph("""
The terminal is a powerful interface for interacting with computers. Understanding command 
line basics is essential for programming and system administration.
""", normal_style))

topics_1_5 = [
    "What is a Terminal/Command Prompt?",
    "Navigating file systems: cd, ls/dir, pwd",
    "Creating files and directories: touch, mkdir",
    "Deleting and moving files: rm, mv",
    "Running Python programs from terminal",
    "Installing packages using pip",
    "Environment variables and PATH",
    "Basic terminal commands for Windows, Mac, and Linux"
]
for topic in topics_1_5:
    elements.append(Paragraph(f"• {topic}", normal_style))

elements.append(Paragraph("<b>Chapter 1 Practical Activities:</b>", normal_style))
activities_ch1 = [
    "Set up Python and VS Code on your computer",
    "Create a project folder and navigate using terminal",
    "Write and run a 'Hello World' program",
    "Create a flowchart for a simple everyday task",
    "Explore AI applications: Try using a chatbot or image recognition tool"
]
for activity in activities_ch1:
    elements.append(Paragraph(f"✓ {activity}", normal_style))

elements.append(PageBreak())

# CHAPTER 2: Python Programming
elements.append(Paragraph("CHAPTER 2", heading1_style))
elements.append(Paragraph("Python Programming Fundamentals", heading1_style))
elements.append(Spacer(1, 0.2*inch))

elements.append(Paragraph("2.1 Introduction to Python", heading2_style))
elements.append(Paragraph("""
Python is one of the most popular programming languages due to its simplicity and versatility. 
It's used in web development, data science, AI, automation, and more.
""", normal_style))

topics_2_1 = [
    "Why Python? - Advantages and use cases",
    "Python installation and setup verification",
    "Interactive Python shell (REPL)",
    "Python syntax rules and indentation",
    "Comments in Python (single-line and multi-line)",
    "print() function and basic output",
    "input() function for user interaction"
]
for topic in topics_2_1:
    elements.append(Paragraph(f"• {topic}", normal_style))

elements.append(Spacer(1, 0.15*inch))
elements.append(Paragraph("2.2 Variables and Data Types", heading2_style))
elements.append(Paragraph("""
Variables store data that programs can manipulate. Understanding data types is crucial for 
writing effective programs.
""", normal_style))

topics_2_2 = [
    "What are variables? - Naming conventions",
    "Data types: int, float, str, bool",
    "Type conversion: int(), float(), str()",
    "Dynamic typing in Python",
    "Multiple variable assignment",
    "Constants and naming best practices",
    "String operations: concatenation, slicing, formatting"
]
for topic in topics_2_2:
    elements.append(Paragraph(f"• {topic}", normal_style))

elements.append(Spacer(1, 0.15*inch))
elements.append(Paragraph("2.3 Operators and Expressions", heading2_style))

topics_2_3 = [
    "Arithmetic operators: +, -, *, /, //, %, **",
    "Comparison operators: ==, !=, <, >, <=, >=",
    "Logical operators: and, or, not",
    "Assignment operators: =, +=, -=, *=, /=",
    "Operator precedence and evaluation order"
]
for topic in topics_2_3:
    elements.append(Paragraph(f"• {topic}", normal_style))

elements.append(Spacer(1, 0.15*inch))
elements.append(Paragraph("2.4 Control Flow: If-Else Statements", heading2_style))
elements.append(Paragraph("""
Control flow statements allow programs to make decisions based on conditions, enabling 
dynamic and responsive behavior.
""", normal_style))

topics_2_4 = [
    "Boolean expressions and conditions",
    "if statement syntax",
    "if-else statements",
    "elif (else if) for multiple conditions",
    "Nested if statements",
    "Ternary conditional expressions",
    "Practical examples: Grade calculator, age validator, number comparator"
]
for topic in topics_2_4:
    elements.append(Paragraph(f"• {topic}", normal_style))

elements.append(Spacer(1, 0.15*inch))
elements.append(Paragraph("2.5 Loops and Iteration", heading2_style))
elements.append(Paragraph("""
Loops enable programs to repeat tasks efficiently without writing redundant code.
""", normal_style))

topics_2_5 = [
    "for loop: Iterating over sequences",
    "range() function for number sequences",
    "while loop: Condition-based repetition",
    "break and continue statements",
    "Nested loops",
    "Loop control best practices",
    "Common loop patterns: counting, accumulation, searching"
]
for topic in topics_2_5:
    elements.append(Paragraph(f"• {topic}", normal_style))

elements.append(Spacer(1, 0.15*inch))
elements.append(Paragraph("2.6 Data Structures", heading2_style))

topics_2_6 = [
    "Lists: Creation, indexing, slicing",
    "List methods: append(), insert(), remove(), pop(), sort()",
    "Tuples: Immutable sequences",
    "Dictionaries: Key-value pairs",
    "Sets: Unique collections",
    "Choosing the right data structure"
]
for topic in topics_2_6:
    elements.append(Paragraph(f"• {topic}", normal_style))

elements.append(Spacer(1, 0.15*inch))
elements.append(Paragraph("2.7 Functions", heading2_style))
elements.append(Paragraph("""
Functions are reusable blocks of code that perform specific tasks, promoting code 
organization and reusability.
""", normal_style))

topics_2_7 = [
    "Defining functions with def keyword",
    "Function parameters and arguments",
    "Return values",
    "Default parameters",
    "Scope: Local vs. Global variables",
    "Lambda functions (anonymous functions)",
    "Docstrings for function documentation",
    "Built-in functions: len(), max(), min(), sum()"
]
for topic in topics_2_7:
    elements.append(Paragraph(f"• {topic}", normal_style))

elements.append(Spacer(1, 0.15*inch))
elements.append(Paragraph("2.8 File Handling", heading2_style))
elements.append(Paragraph("""
File operations enable programs to read from and write to files, essential for data 
persistence and processing.
""", normal_style))

topics_2_8 = [
    "Opening files: open() function",
    "File modes: 'r' (read), 'w' (write), 'a' (append)",
    "Reading files: read(), readline(), readlines()",
    "Writing to files: write(), writelines()",
    "Closing files and resource management",
    "Using 'with' statement for automatic file closing",
    "Working with CSV files",
    "File paths: Absolute vs. Relative"
]
for topic in topics_2_8:
    elements.append(Paragraph(f"• {topic}", normal_style))

elements.append(Spacer(1, 0.15*inch))
elements.append(Paragraph("2.9 Exception Handling", heading2_style))
elements.append(Paragraph("""
Exception handling prevents programs from crashing and provides graceful error management.
""", normal_style))

topics_2_9 = [
    "Understanding errors and exceptions",
    "try-except blocks",
    "Catching specific exceptions: ValueError, TypeError, FileNotFoundError",
    "Multiple except clauses",
    "else clause in exception handling",
    "finally clause for cleanup operations",
    "Raising exceptions manually",
    "Best practices for error handling"
]
for topic in topics_2_9:
    elements.append(Paragraph(f"• {topic}", normal_style))

elements.append(Paragraph("<b>Chapter 2 Practical Projects:</b>", normal_style))
projects_ch2 = [
    "Calculator program with basic operations",
    "Student grade management system",
    "Simple contact book using file storage",
    "Number guessing game",
    "Temperature converter (Celsius/Fahrenheit)",
    "To-do list application with file persistence"
]
for project in projects_ch2:
    elements.append(Paragraph(f"✓ {project}", normal_style))

elements.append(PageBreak())

# CHAPTER 3: Cloud Computing
elements.append(Paragraph("CHAPTER 3", heading1_style))
elements.append(Paragraph("Cloud Computing Essentials", heading1_style))
elements.append(Spacer(1, 0.2*inch))

elements.append(Paragraph("3.1 Introduction to Cloud Computing", heading2_style))
elements.append(Paragraph("""
Cloud computing has revolutionized how we store data, run applications, and deliver services. 
Understanding cloud fundamentals is essential in today's digital landscape.
""", normal_style))

topics_3_1 = [
    "What is Cloud Computing? - Definition and concept",
    "Evolution from traditional computing to cloud",
    "Cloud vs. On-premises infrastructure",
    "Real-world examples: Gmail, Netflix, Dropbox, Google Drive",
    "Benefits: Scalability, Cost-efficiency, Accessibility, Reliability",
    "Challenges: Security concerns, Internet dependency, Vendor lock-in"
]
for topic in topics_3_1:
    elements.append(Paragraph(f"• {topic}", normal_style))

elements.append(Spacer(1, 0.15*inch))
elements.append(Paragraph("3.2 Cloud Service Models", heading2_style))
elements.append(Paragraph("""
Cloud services are delivered through different models, each offering varying levels of 
control and management.
""", normal_style))

topics_3_2 = [
    "<b>Infrastructure as a Service (IaaS)</b>: Virtual machines, storage, networks",
    "Examples: Amazon EC2, Google Compute Engine, Microsoft Azure VMs",
    "<b>Platform as a Service (PaaS)</b>: Development platforms and tools",
    "Examples: Google App Engine, Heroku, AWS Elastic Beanstalk",
    "<b>Software as a Service (SaaS)</b>: Ready-to-use applications",
    "Examples: Google Workspace, Microsoft 365, Salesforce, Zoom",
    "Comparison: When to use IaaS vs PaaS vs SaaS"
]
for topic in topics_3_2:
    elements.append(Paragraph(f"• {topic}", normal_style))

elements.append(Spacer(1, 0.15*inch))
elements.append(Paragraph("3.3 Cloud Deployment Models", heading2_style))

topics_3_3 = [
    "<b>Public Cloud</b>: Shared resources, cost-effective (AWS, Azure, GCP)",
    "<b>Private Cloud</b>: Dedicated infrastructure for single organization",
    "<b>Hybrid Cloud</b>: Combination of public and private clouds",
    "<b>Multi-Cloud</b>: Using multiple cloud providers",
    "Community Cloud: Shared by organizations with common requirements",
    "Choosing the right deployment model"
]
for topic in topics_3_3:
    elements.append(Paragraph(f"• {topic}", normal_style))

elements.append(Spacer(1, 0.15*inch))
elements.append(Paragraph("3.4 Major Cloud Platforms", heading2_style))

topics_3_4 = [
    "<b>Amazon Web Services (AWS)</b>: Market leader, comprehensive services",
    "AWS services: EC2, S3, Lambda, RDS",
    "<b>Microsoft Azure</b>: Enterprise integration, Windows ecosystem",
    "Azure services: Virtual Machines, Blob Storage, Azure Functions",
    "<b>Google Cloud Platform (GCP)</b>: Data analytics and AI strengths",
    "GCP services: Compute Engine, Cloud Storage, BigQuery",
    "Other providers: IBM Cloud, Oracle Cloud, Alibaba Cloud"
]
for topic in topics_3_4:
    elements.append(Paragraph(f"• {topic}", normal_style))

elements.append(Spacer(1, 0.15*inch))
elements.append(Paragraph("3.5 Cloud Storage Solutions", heading2_style))
elements.append(Paragraph("""
Cloud storage enables users to store and access data from anywhere with internet connectivity.
""", normal_style))

topics_3_5 = [
    "Types of cloud storage: Object, Block, File storage",
    "Google Drive: Features and collaboration tools",
    "Dropbox: File synchronization and sharing",
    "OneDrive: Microsoft ecosystem integration",
    "iCloud: Apple device synchronization",
    "Storage tiers: Hot, Cool, Archive storage",
    "Data redundancy and backup strategies"
]
for topic in topics_3_5:
    elements.append(Paragraph(f"• {topic}", normal_style))

elements.append(Spacer(1, 0.15*inch))
elements.append(Paragraph("3.6 Cloud Security & Privacy", heading2_style))

topics_3_6 = [
    "Shared responsibility model in cloud security",
    "Data encryption: At rest and in transit",
    "Identity and Access Management (IAM)",
    "Multi-factor authentication (MFA)",
    "Compliance standards: GDPR, HIPAA, SOC 2",
    "Data sovereignty and location",
    "Backup and disaster recovery in cloud"
]
for topic in topics_3_6:
    elements.append(Paragraph(f"• {topic}", normal_style))

elements.append(Spacer(1, 0.15*inch))
elements.append(Paragraph("3.7 Cloud Computing Applications", heading2_style))

topics_3_7 = [
    "Web hosting and application deployment",
    "Big data processing and analytics",
    "Machine learning and AI model training",
    "Content delivery networks (CDN)",
    "Collaboration tools: Google Workspace, Microsoft Teams",
    "IoT (Internet of Things) data processing",
    "Gaming and streaming services",
    "Development and testing environments"
]
for topic in topics_3_7:
    elements.append(Paragraph(f"• {topic}", normal_style))

elements.append(Spacer(1, 0.15*inch))
elements.append(Paragraph("3.8 Emerging Cloud Technologies", heading2_style))

topics_3_8 = [
    "Serverless computing: AWS Lambda, Azure Functions",
    "Containers and Kubernetes",
    "Edge computing: Processing data closer to source",
    "Cloud-native applications",
    "AI and ML services in cloud (AWS SageMaker, Google AI Platform)",
    "Quantum computing in the cloud"
]
for topic in topics_3_8:
    elements.append(Paragraph(f"• {topic}", normal_style))

elements.append(Paragraph("<b>Chapter 3 Hands-on Activities:</b>", normal_style))
activities_ch3 = [
    "Create a Google Cloud or AWS free tier account",
    "Set up cloud storage and organize files",
    "Deploy a simple static website to cloud hosting",
    "Explore cloud-based collaboration using Google Docs",
    "Compare pricing models of different cloud providers",
    "Create a disaster recovery plan for cloud data"
]
for activity in activities_ch3:
    elements.append(Paragraph(f"✓ {activity}", normal_style))

elements.append(PageBreak())

# CHAPTER 4: Networking Fundamentals
elements.append(Paragraph("CHAPTER 4", heading1_style))
elements.append(Paragraph("Networking Fundamentals & Protocols", heading1_style))
elements.append(Spacer(1, 0.2*inch))

elements.append(Paragraph("4.1 Introduction to Computer Networks", heading2_style))
elements.append(Paragraph("""
Computer networks form the backbone of modern communication, enabling devices to share 
resources and information globally.
""", normal_style))

topics_4_1 = [
    "What is a computer network?",
    "Types of networks: LAN, WAN, MAN, PAN",
    "Wired vs. Wireless networks",
    "Network topologies: Star, Bus, Ring, Mesh",
    "Network components: Routers, Switches, Hubs, Access Points",
    "Client-Server vs. Peer-to-Peer architecture",
    "Bandwidth and latency concepts"
]
for topic in topics_4_1:
    elements.append(Paragraph(f"• {topic}", normal_style))

elements.append(Spacer(1, 0.15*inch))
elements.append(Paragraph("4.2 The OSI Model (7 Layers)", heading2_style))
elements.append(Paragraph("""
The OSI (Open Systems Interconnection) model is a conceptual framework that standardizes 
network communication functions into seven distinct layers.
""", normal_style))

topics_4_2 = [
    "<b>Layer 7 - Application Layer</b>: User interfaces and applications (HTTP, FTP, SMTP, DNS)",
    "<b>Layer 6 - Presentation Layer</b>: Data formatting, encryption, compression",
    "<b>Layer 5 - Session Layer</b>: Session establishment and management",
    "<b>Layer 4 - Transport Layer</b>: End-to-end communication (TCP, UDP)",
    "<b>Layer 3 - Network Layer</b>: Routing and IP addressing",
    "<b>Layer 2 - Data Link Layer</b>: MAC addressing, error detection",
    "<b>Layer 1 - Physical Layer</b>: Physical transmission of bits",
    "Mnemonic: 'Please Do Not Throw Sausage Pizza Away'",
    "Encapsulation and decapsulation process"
]
for topic in topics_4_2:
    elements.append(Paragraph(f"• {topic}", normal_style))

elements.append(Spacer(1, 0.15*inch))
elements.append(Paragraph("4.3 TCP/IP Model", heading2_style))
elements.append(Paragraph("""
The TCP/IP model is the practical implementation used in the Internet, condensing the OSI 
model into four functional layers.
""", normal_style))

topics_4_3 = [
    "<b>Application Layer</b>: Combines OSI layers 5, 6, 7",
    "<b>Transport Layer</b>: TCP (reliable) and UDP (fast)",
    "<b>Internet Layer</b>: IP routing and addressing (IPv4, IPv6)",
    "<b>Network Access Layer</b>: Physical and data link combined",
    "Comparison: OSI vs TCP/IP models",
    "Why TCP/IP became the standard",
    "TCP three-way handshake: SYN, SYN-ACK, ACK",
    "UDP use cases: Streaming, gaming, DNS queries"
]
for topic in topics_4_3:
    elements.append(Paragraph(f"• {topic}", normal_style))

elements.append(Spacer(1, 0.15*inch))
elements.append(Paragraph("4.4 IP Addressing", heading2_style))

topics_4_4 = [
    "What is an IP address?",
    "IPv4: Structure (32-bit, dotted decimal notation)",
    "Public vs. Private IP addresses",
    "IP address classes: Class A, B, C, D, E",
    "Subnetting basics and subnet masks",
    "IPv6: Need and structure (128-bit)",
    "Dynamic (DHCP) vs. Static IP assignment",
    "NAT (Network Address Translation)",
    "Finding your IP address: ipconfig/ifconfig"
]
for topic in topics_4_4:
    elements.append(Paragraph(f"• {topic}", normal_style))

elements.append(Spacer(1, 0.15*inch))
elements.append(Paragraph("4.5 Essential Network Protocols", heading2_style))
elements.append(Paragraph("""
Protocols define the rules and standards for network communication.
""", normal_style))

topics_4_5 = [
    "<b>HTTP/HTTPS</b>: Web browsing and secure communication",
    "<b>FTP/SFTP</b>: File transfer protocols",
    "<b>SMTP/POP3/IMAP</b>: Email protocols",
    "<b>DNS</b>: Domain Name System - Translating names to IP addresses",
    "<b>DHCP</b>: Dynamic IP address assignment",
    "<b>ARP</b>: Address Resolution Protocol - IP to MAC mapping",
    "<b>ICMP</b>: Internet Control Message Protocol - Ping and error reporting",
    "<b>SSH</b>: Secure Shell for remote access",
    "<b>Telnet</b>: Remote terminal access (insecure)",
    "Port numbers and well-known ports (80, 443, 21, 22, 25)"
]
for topic in topics_4_5:
    elements.append(Paragraph(f"• {topic}", normal_style))

elements.append(Spacer(1, 0.15*inch))
elements.append(Paragraph("4.6 Network Tools and Commands", heading2_style))
elements.append(Paragraph("""
Network administrators use various command-line tools to troubleshoot and manage networks.
""", normal_style))

topics_4_6 = [
    "<b>ping</b>: Test connectivity and measure latency",
    "<b>traceroute/tracert</b>: Trace the path packets take",
    "<b>ipconfig/ifconfig</b>: View network configuration",
    "<b>nslookup/dig</b>: DNS query tools",
    "<b>netstat</b>: Display network connections and statistics",
    "<b>arp</b>: View and modify ARP cache",
    "<b>route</b>: View and modify routing table",
    "<b>Wireshark</b>: Network packet analyzer",
    "Practical troubleshooting scenarios"
]
for topic in topics_4_6:
    elements.append(Paragraph(f"• {topic}", normal_style))

elements.append(Spacer(1, 0.15*inch))
elements.append(Paragraph("4.7 API (Application Programming Interface)", heading2_style))
elements.append(Paragraph("""
APIs enable different software applications to communicate and exchange data.
""", normal_style))

topics_4_7 = [
    "What is an API? - Concept and purpose",
    "REST API architecture and principles",
    "HTTP methods: GET, POST, PUT, DELETE",
    "JSON and XML data formats",
    "API endpoints and requests",
    "Authentication: API keys, OAuth, JWT",
    "Status codes: 200 (OK), 404 (Not Found), 500 (Server Error)",
    "Testing APIs with Postman or curl",
    "Real-world examples: Weather API, Google Maps API, Twitter API"
]
for topic in topics_4_7:
    elements.append(Paragraph(f"• {topic}", normal_style))

elements.append(Spacer(1, 0.15*inch))
elements.append(Paragraph("4.8 Wireless Networks and Wi-Fi", heading2_style))

topics_4_8 = [
    "Wi-Fi standards: 802.11a/b/g/n/ac/ax (Wi-Fi 6)",
    "Frequency bands: 2.4 GHz vs. 5 GHz",
    "SSID, WPA2/WPA3 encryption",
    "Bluetooth technology and applications",
    "Mobile networks: 3G, 4G LTE, 5G",
    "IoT connectivity: LoRa, Zigbee, NFC"
]
for topic in topics_4_8:
    elements.append(Paragraph(f"• {topic}", normal_style))

elements.append(Paragraph("<b>Chapter 4 Practical Labs:</b>", normal_style))
labs_ch4 = [
    "Use ping and traceroute to diagnose network issues",
    "Analyze network packets using Wireshark",
    "Set up a simple REST API request using Python",
    "Configure a home network with proper security",
    "Create a network diagram for your school/home",
    "Perform DNS lookup and understand results"
]
for lab in labs_ch4:
    elements.append(Paragraph(f"✓ {lab}", normal_style))

elements.append(PageBreak())

# CHAPTER 5: Cybersecurity
elements.append(Paragraph("CHAPTER 5", heading1_style))
elements.append(Paragraph("Cybersecurity & Threat Protection", heading1_style))
elements.append(Spacer(1, 0.2*inch))

elements.append(Paragraph("5.1 Introduction to Cybersecurity", heading2_style))
elements.append(Paragraph("""
Cybersecurity protects systems, networks, and data from digital attacks, unauthorized 
access, and damage. In our connected world, security awareness is critical.
""", normal_style))

topics_5_1 = [
    "What is Cybersecurity? - Definition and importance",
    "The CIA Triad: Confidentiality, Integrity, Availability",
    "Types of cyber threats: Malware, Phishing, DDoS, Ransomware",
    "Evolution of cyber threats",
    "Famous cyber attacks: WannaCry, Equifax breach, SolarWinds",
    "Career paths in cybersecurity",
    "Cybersecurity frameworks: NIST, ISO 27001"
]
for topic in topics_5_1:
    elements.append(Paragraph(f"• {topic}", normal_style))

elements.append(Spacer(1, 0.15*inch))
elements.append(Paragraph("5.2 Phishing Attacks", heading2_style))
elements.append(Paragraph("""
Phishing is one of the most common cyber attacks, tricking users into revealing sensitive 
information or installing malware.
""", normal_style))

topics_5_2 = [
    "What is Phishing? - Social engineering technique",
    "Types of phishing: Email, SMS (Smishing), Voice (Vishing), Spear phishing",
    "Identifying phishing emails: Suspicious sender, urgent language, grammatical errors",
    "URL inspection: Hover before clicking, check for HTTPS",
    "Fake websites and lookalike domains",
    "Whaling attacks: Targeting executives",
    "Preventing phishing: Email filters, awareness training, verification",
    "What to do if you fall victim to phishing",
    "Real-world phishing case studies"
]
for topic in topics_5_2:
    elements.append(Paragraph(f"• {topic}", normal_style))

elements.append(Spacer(1, 0.15*inch))
elements.append(Paragraph("5.3 Malware and Threats", heading2_style))
elements.append(Paragraph("""
Malware (malicious software) encompasses various harmful programs designed to damage, 
disrupt, or gain unauthorized access to systems.
""", normal_style))

topics_5_3 = [
    "<b>Viruses</b>: Self-replicating programs that infect files",
    "<b>Worms</b>: Standalone malware that spreads across networks",
    "<b>Trojans</b>: Disguised as legitimate software",
    "<b>Ransomware</b>: Encrypts data and demands payment",
    "<b>Spyware</b>: Secretly monitors user activity",
    "<b>Adware</b>: Unwanted advertisements",
    "<b>Rootkits</b>: Hidden malware with admin privileges",
    "<b>Keyloggers</b>: Records keystrokes to steal credentials",
    "How malware spreads: Email attachments, infected downloads, USB drives",
    "Malware detection and removal"
]
for topic in topics_5_3:
    elements.append(Paragraph(f"• {topic}", normal_style))

elements.append(Spacer(1, 0.15*inch))
elements.append(Paragraph("5.4 Protection Strategies", heading2_style))
elements.append(Paragraph("""
Implementing multiple layers of security significantly reduces risk and protects against 
various attack vectors.
""", normal_style))

topics_5_4 = [
    "<b>Antivirus software</b>: Real-time scanning and threat detection",
    "<b>Firewalls</b>: Network traffic filtering (hardware and software)",
    "<b>VPN (Virtual Private Network)</b>: Encrypted internet connection",
    "<b>Regular software updates</b>: Patching security vulnerabilities",
    "<b>Strong passwords</b>: Complexity, length, uniqueness",
    "<b>Multi-factor authentication (MFA)</b>: Additional verification layer",
    "<b>Encryption</b>: Protecting data at rest and in transit",
    "<b>Backup strategies</b>: 3-2-1 backup rule",
    "<b>Security awareness training</b>: Human firewall",
    "<b>Least privilege principle</b>: Minimal necessary access"
]
for topic in topics_5_4:
    elements.append(Paragraph(f"• {topic}", normal_style))

elements.append(Spacer(1, 0.15*inch))
elements.append(Paragraph("5.5 Password Security", heading2_style))

topics_5_5 = [
    "Creating strong passwords: 12+ characters, mixed case, numbers, symbols",
    "Password managers: Benefits and recommended tools",
    "Two-factor authentication (2FA) setup",
    "Biometric authentication: Fingerprint, Face ID",
    "Common password mistakes to avoid",
    "Password breach checking: Have I Been Pwned",
    "Social media security settings"
]
for topic in topics_5_5:
    elements.append(Paragraph(f"• {topic}", normal_style))

elements.append(Spacer(1, 0.15*inch))
elements.append(Paragraph("5.6 Network Security", heading2_style))

topics_5_6 = [
    "Securing Wi-Fi networks: WPA3 encryption, strong passwords",
    "Intrusion Detection Systems (IDS) and Prevention Systems (IPS)",
    "DMZ (Demilitarized Zone) in network architecture",
    "Port scanning and vulnerability assessment",
    "Secure remote access: SSH, VPN",
    "Network segmentation and VLANs",
    "Honeypots: Deception technology"
]
for topic in topics_5_6:
    elements.append(Paragraph(f"• {topic}", normal_style))

elements.append(Spacer(1, 0.15*inch))
elements.append(Paragraph("5.7 Ethical Hacking and Penetration Testing", heading2_style))

topics_5_7 = [
    "What is ethical hacking? - White hat vs. Black hat",
    "Penetration testing methodology",
    "Reconnaissance and information gathering",
    "Vulnerability scanning tools: Nmap, Nessus",
    "Exploitation frameworks: Metasploit basics",
    "Bug bounty programs",
    "Legal and ethical considerations",
    "Certifications: CEH, OSCP, Security+"
]
for topic in topics_5_7:
    elements.append(Paragraph(f"• {topic}", normal_style))

elements.append(Spacer(1, 0.15*inch))
elements.append(Paragraph("5.8 Digital Footprint and Privacy", heading2_style))

topics_5_8 = [
    "Understanding your digital footprint",
    "Privacy settings on social media platforms",
    "Safe browsing practices",
    "Private browsing and incognito mode",
    "Data collection by websites and apps",
    "GDPR and privacy regulations",
    "Identity theft prevention",
    "Safe online shopping practices"
]
for topic in topics_5_8:
    elements.append(Paragraph(f"• {topic}", normal_style))

elements.append(Paragraph("<b>Chapter 5 Security Projects:</b>", normal_style))
projects_ch5 = [
    "Create a security audit checklist for personal devices",
    "Set up a password manager and migrate passwords",
    "Conduct a phishing simulation exercise",
    "Configure firewall rules on your computer",
    "Create a data backup and recovery plan",
    "Analyze network traffic for security threats"
]
for project in projects_ch5:
    elements.append(Paragraph(f"✓ {project}", normal_style))

elements.append(PageBreak())

# CHAPTER 6: Data Structures & Algorithms
elements.append(Paragraph("CHAPTER 6", heading1_style))
elements.append(Paragraph("Data Structures & Algorithms Basics", heading1_style))
elements.append(Spacer(1, 0.2*inch))

elements.append(Paragraph("6.1 Introduction to Data Structures", heading2_style))
elements.append(Paragraph("""
Data structures organize and store data efficiently, enabling effective data manipulation 
and access. Choosing the right data structure is crucial for program performance.
""", normal_style))

topics_6_1 = [
    "What are Data Structures? - Importance in programming",
    "Abstract Data Types (ADT)",
    "Linear vs. Non-linear data structures",
    "Static vs. Dynamic data structures",
    "Time complexity and Big O notation basics",
    "Space complexity considerations"
]
for topic in topics_6_1:
    elements.append(Paragraph(f"• {topic}", normal_style))

elements.append(Spacer(1, 0.15*inch))
elements.append(Paragraph("6.2 Stack", heading2_style))
elements.append(Paragraph("""
A stack is a Last-In-First-Out (LIFO) data structure, like a stack of plates where you 
can only add or remove from the top.
""", normal_style))

topics_6_2 = [
    "Stack concept and LIFO principle",
    "Stack operations: push, pop, peek, isEmpty",
    "Implementing stack using lists in Python",
    "Stack applications: Browser history, Undo functionality, Expression evaluation",
    "Balanced parentheses problem",
    "Function call stack",
    "Time complexity: O(1) for push and pop"
]
for topic in topics_6_2:
    elements.append(Paragraph(f"• {topic}", normal_style))

elements.append(Spacer(1, 0.15*inch))
elements.append(Paragraph("6.3 Queue", heading2_style))
elements.append(Paragraph("""
A queue is a First-In-First-Out (FIFO) data structure, like people waiting in line.
""", normal_style))

topics_6_3 = [
    "Queue concept and FIFO principle",
    "Queue operations: enqueue, dequeue, front, isEmpty",
    "Implementing queue using lists and collections.deque",
    "Types: Simple queue, Circular queue, Priority queue, Double-ended queue (Deque)",
    "Queue applications: Print spooler, Task scheduling, BFS algorithm",
    "Real-world examples: Customer service, CPU scheduling"
]
for topic in topics_6_3:
    elements.append(Paragraph(f"• {topic}", normal_style))

elements.append(Spacer(1, 0.15*inch))
elements.append(Paragraph("6.4 Linked Lists (Introduction)", heading2_style))

topics_6_4 = [
    "What is a Linked List? - Nodes and pointers",
    "Singly vs. Doubly linked lists",
    "Advantages over arrays: Dynamic size, efficient insertion/deletion",
    "Disadvantages: No random access, extra memory for pointers",
    "Basic operations: Insertion, deletion, traversal",
    "Applications: Music playlists, Browser navigation"
]
for topic in topics_6_4:
    elements.append(Paragraph(f"• {topic}", normal_style))

elements.append(Spacer(1, 0.15*inch))
elements.append(Paragraph("6.5 Introduction to Algorithms", heading2_style))
elements.append(Paragraph("""
Algorithms are step-by-step procedures for solving problems. Efficient algorithms are 
essential for optimal program performance.
""", normal_style))

topics_6_5 = [
    "What is an Algorithm? - Characteristics: Finiteness, Definiteness, Input/Output",
    "Algorithm analysis: Best, Average, Worst case",
    "Big O notation: O(1), O(log n), O(n), O(n log n), O(n²)",
    "Space-time trade-offs"
]
for topic in topics_6_5:
    elements.append(Paragraph(f"• {topic}", normal_style))

elements.append(Spacer(1, 0.15*inch))
elements.append(Paragraph("6.6 Searching Algorithms", heading2_style))

topics_6_6 = [
    "<b>Linear Search</b>: Sequential checking, O(n) complexity",
    "<b>Binary Search</b>: Divide and conquer on sorted data, O(log n)",
    "Implementing both in Python",
    "When to use which algorithm",
    "Practical comparison and performance analysis"
]
for topic in topics_6_6:
    elements.append(Paragraph(f"• {topic}", normal_style))

elements.append(Spacer(1, 0.15*inch))
elements.append(Paragraph("6.7 Sorting Algorithms (Basics)", heading2_style))

topics_6_7 = [
    "<b>Bubble Sort</b>: Simple comparison-based, O(n²)",
    "<b>Selection Sort</b>: Finding minimum element, O(n²)",
    "<b>Insertion Sort</b>: Building sorted array, O(n²)",
    "Introduction to efficient sorts: Quick Sort, Merge Sort",
    "Python's built-in sort() and sorted()",
    "Stability in sorting algorithms"
]
for topic in topics_6_7:
    elements.append(Paragraph(f"• {topic}", normal_style))

elements.append(Paragraph("<b>Chapter 6 Coding Exercises:</b>", normal_style))
exercises_ch6 = [
    "Implement a stack to reverse a string",
    "Create a queue simulation for a ticket counter",
    "Write binary search algorithm",
    "Implement bubble sort and measure performance",
    "Solve balanced parentheses problem using stack",
    "Compare time complexity of different sorting algorithms"
]
for exercise in exercises_ch6:
    elements.append(Paragraph(f"✓ {exercise}", normal_style))

elements.append(PageBreak())

# CHAPTER 7: Advanced Topics & Java/JavaScript
elements.append(Paragraph("CHAPTER 7", heading1_style))
elements.append(Paragraph("Introduction to Java, JavaScript & Course Revision", heading1_style))
elements.append(Spacer(1, 0.2*inch))

elements.append(Paragraph("7.1 Introduction to Java", heading2_style))
elements.append(Paragraph("""
Java is a popular, object-oriented programming language known for its "write once, run 
anywhere" philosophy. It's widely used in enterprise applications, Android development, 
and web services.
""", normal_style))

topics_7_1 = [
    "What is Java? - History and features",
    "Java vs. Python: Syntax and paradigm differences",
    "JVM (Java Virtual Machine) and platform independence",
    "Installing JDK (Java Development Kit)",
    "First Java program: Hello World",
    "Basic syntax: Classes, methods, main function",
    "Data types and variables in Java",
    "Control structures: if-else, loops",
    "Java development tools: Eclipse, IntelliJ IDEA"
]
for topic in topics_7_1:
    elements.append(Paragraph(f"• {topic}", normal_style))

elements.append(Spacer(1, 0.15*inch))
elements.append(Paragraph("7.2 Object-Oriented Programming in Java", heading2_style))

topics_7_2 = [
    "Classes and Objects",
    "Constructors and methods",
    "Encapsulation and access modifiers",
    "Inheritance basics",
    "Polymorphism introduction",
    "Simple Java projects: Calculator, Student management system"
]
for topic in topics_7_2:
    elements.append(Paragraph(f"• {topic}", normal_style))

elements.append(Spacer(1, 0.15*inch))
elements.append(Paragraph("7.3 Introduction to JavaScript", heading2_style))
elements.append(Paragraph("""
JavaScript is the programming language of the web, enabling interactive and dynamic 
websites. It runs in browsers and, with Node.js, on servers too.
""", normal_style))

topics_7_3 = [
    "What is JavaScript? - Role in web development",
    "JavaScript vs. Java: Key differences",
    "Adding JavaScript to HTML: inline, internal, external",
    "Variables: var, let, const",
    "Data types and operators",
    "Functions and arrow functions",
    "DOM (Document Object Model) manipulation",
    "Event handling: onclick, onload",
    "Console and debugging tools"
]
for topic in topics_7_3:
    elements.append(Paragraph(f"• {topic}", normal_style))

elements.append(Spacer(1, 0.15*inch))
elements.append(Paragraph("7.4 JavaScript for Interactive Websites", heading2_style))

topics_7_4 = [
    "Form validation",
    "Interactive buttons and elements",
    "Simple animations and effects",
    "Working with arrays and objects",
    "Introduction to JSON",
    "Basic AJAX and fetch API",
    "Mini project: Interactive calculator or quiz application"
]
for topic in topics_7_4:
    elements.append(Paragraph(f"• {topic}", normal_style))

elements.append(Spacer(1, 0.15*inch))
elements.append(Paragraph("7.5 Data Analysis Introduction", heading2_style))
elements.append(Paragraph("""
Data analysis transforms raw data into meaningful insights using Python libraries.
""", normal_style))

topics_7_5 = [
    "Introduction to Pandas library",
    "Reading CSV and Excel files",
    "Data cleaning and preprocessing",
    "Basic statistics: mean, median, mode",
    "Data visualization with Matplotlib",
    "Creating bar charts, line plots, and pie charts",
    "Mini project: Analyze student grades or weather data"
]
for topic in topics_7_5:
    elements.append(Paragraph(f"• {topic}", normal_style))

elements.append(PageBreak())

# Revision Section
elements.append(Paragraph("COMPREHENSIVE REVISION & PRACTICE", heading1_style))
elements.append(Spacer(1, 0.2*inch))

elements.append(Paragraph("7.6 Chapter-wise Key Concepts Summary", heading2_style))

elements.append(Paragraph("<b>Chapter 1 - Technology Foundations:</b>", heading3_style))
revision_ch1 = [
    "Computer components and binary system",
    "AI applications and ethics",
    "Programming basics and algorithms",
    "VS Code setup and terminal commands"
]
for item in revision_ch1:
    elements.append(Paragraph(f"• {item}", normal_style))

elements.append(Paragraph("<b>Chapter 2 - Python Programming:</b>", heading3_style))
revision_ch2 = [
    "Variables, data types, and operators",
    "Control flow: if-else, loops",
    "Functions and scope",
    "File handling and exception management"
]
for item in revision_ch2:
    elements.append(Paragraph(f"• {item}", normal_style))

elements.append(Paragraph("<b>Chapter 3 - Cloud Computing:</b>", heading3_style))
revision_ch3 = [
    "IaaS, PaaS, SaaS models",
    "Major cloud platforms: AWS, Azure, GCP",
    "Cloud storage and security",
    "Emerging cloud technologies"
]
for item in revision_ch3:
    elements.append(Paragraph(f"• {item}", normal_style))

elements.append(Paragraph("<b>Chapter 4 - Networking:</b>", heading3_style))
revision_ch4 = [
    "OSI and TCP/IP models",
    "IP addressing and subnetting",
    "Network protocols: HTTP, DNS, DHCP",
    "API concepts and REST architecture"
]
for item in revision_ch4:
    elements.append(Paragraph(f"• {item}", normal_style))

elements.append(Paragraph("<b>Chapter 5 - Cybersecurity:</b>", heading3_style))
revision_ch5 = [
    "Phishing identification and prevention",
    "Malware types and protection",
    "Password security and MFA",
    "Network security and ethical hacking"
]
for item in revision_ch5:
    elements.append(Paragraph(f"• {item}", normal_style))

elements.append(Paragraph("<b>Chapter 6 - DSA:</b>", heading3_style))
revision_ch6 = [
    "Stack (LIFO) and Queue (FIFO)",
    "Linked lists basics",
    "Searching: Linear and Binary",
    "Sorting algorithms and complexity"
]
for item in revision_ch6:
    elements.append(Paragraph(f"• {item}", normal_style))

elements.append(PageBreak())

# Practice Questions
elements.append(Paragraph("7.7 Practice Question Sets", heading2_style))
elements.append(Spacer(1, 0.15*inch))

elements.append(Paragraph("<b>Section A: Multiple Choice Questions (30 Questions)</b>", heading3_style))

mcq_questions = [
    "1. Which component is considered the 'brain' of the computer?",
    "   a) RAM  b) CPU  c) Hard Drive  d) GPU",
    "",
    "2. What does AI stand for?",
    "   a) Automated Intelligence  b) Artificial Intelligence",
    "   c) Advanced Integration  d) Algorithmic Information",
    "",
    "3. Which is NOT a valid Python data type?",
    "   a) int  b) float  c) char  d) str",
    "",
    "4. What is the output of: print(5 // 2) in Python?",
    "   a) 2.5  b) 2  c) 3  d) Error",
    "",
    "5. Which cloud service model provides ready-to-use applications?",
    "   a) IaaS  b) PaaS  c) SaaS  d) DaaS",
    "",
    "6. How many layers are in the OSI model?",
    "   a) 4  b) 5  c) 7  d) 9",
    "",
    "7. Which protocol is used for secure web browsing?",
    "   a) HTTP  b) HTTPS  c) FTP  d) SMTP",
    "",
    "8. What does TCP stand for?",
    "   a) Transfer Control Protocol  b) Transmission Control Protocol",
    "   c) Technical Communication Process  d) Transport Connection Protocol",
    "",
    "9. Which is an example of phishing?",
    "   a) DDoS attack  b) Fake email asking for password",
    "   c) Virus infection  d) Hardware failure",
    "",
    "10. What is the time complexity of binary search?",
    "   a) O(n)  b) O(log n)  c) O(n²)  d) O(1)",
    "",
    "11. Which data structure follows LIFO principle?",
    "   a) Queue  b) Stack  c) Array  d) Tree",
    "",
    "12. What is the purpose of a firewall?",
    "   a) Speed up internet  b) Filter network traffic",
    "   c) Store passwords  d) Encrypt files",
    "",
    "13. Which cloud platform is developed by Amazon?",
    "   a) Azure  b) GCP  c) AWS  d) IBM Cloud",
    "",
    "14. What does DNS translate?",
    "   a) IP to MAC  b) Domain names to IP addresses",
    "   c) Binary to decimal  d) HTTP to HTTPS",
    "",
    "15. Which loop executes at least once?",
    "   a) for loop  b) while loop  c) do-while loop  d) nested loop",
    "",
    "16. What is ransomware?",
    "   a) Free software  b) Malware that encrypts data for ransom",
    "   c) Antivirus program  d) Cloud storage service",
    "",
    "17. Which is NOT a Python loop keyword?",
    "   a) for  b) while  c) repeat  d) break",
    "",
    "18. What does API stand for?",
    "   a) Application Programming Interface  b) Advanced Program Integration",
    "   c) Automated Process Interaction  d) Applied Program Installation",
    "",
    "19. Which authentication method is most secure?",
    "   a) Simple password  b) Biometric  c) Multi-factor  d) PIN",
    "",
    "20. What is the correct file mode to append data in Python?",
    "   a) 'r'  b) 'w'  c) 'a'  d) 'x'",
    "",
    "21. Which layer of OSI model handles encryption?",
    "   a) Application  b) Presentation  c) Session  d) Transport",
    "",
    "22. What is the default port for HTTP?",
    "   a) 21  b) 22  c) 80  d) 443",
    "",
    "23. Which sorting algorithm is most efficient?",
    "   a) Bubble sort  b) Quick sort  c) Selection sort  d) All equal",
    "",
    "24. What is cloud computing's main advantage?",
    "   a) Complexity  b) Scalability  c) Higher cost  d) Offline access",
    "",
    "25. Which symbol denotes comments in Python?",
    "   a) //  b) /* */  c) #  d) --",
    "",
    "26. What does VPN stand for?",
    "   a) Virtual Private Network  b) Very Protected Network",
    "   c) Verified Public Node  d) Visual Program Navigation",
    "",
    "27. Which is a private IP address range?",
    "   a) 8.8.8.8  b) 192.168.1.1  c) 1.1.1.1  d) 151.101.1.1",
    "",
    "28. What is a stack overflow?",
    "   a) Too many items in stack  b) Empty stack error",
    "   c) Queue error  d) Sorting error",
    "",
    "29. Which language is NOT object-oriented?",
    "   a) Java  b) Python  c) C  d) JavaScript",
    "",
    "30. What does HTTPS use for encryption?",
    "   a) FTP  b) SSL/TLS  c) UDP  d) ICMP"
]

for q in mcq_questions:
    elements.append(Paragraph(q, normal_style))

elements.append(PageBreak())

elements.append(Paragraph("<b>Section B: Short Answer Questions (20 Questions)</b>", heading3_style))

short_questions = [
    "1. Explain the difference between RAM and ROM.",
    "2. What are the three main components of the CIA Triad in cybersecurity?",
    "3. Write a Python function to check if a number is even or odd.",
    "4. Differentiate between TCP and UDP protocols.",
    "5. What is the difference between IaaS, PaaS, and SaaS?",
    "6. Explain the purpose of the try-except block in Python.",
    "7. List three common network troubleshooting commands.",
    "8. What is the difference between a stack and a queue?",
    "9. Describe two methods to prevent phishing attacks.",
    "10. What is the purpose of an API?",
    "11. Explain Big O notation with an example.",
    "12. What is the difference between IPv4 and IPv6?",
    "13. Write Python code to open and read a text file.",
    "14. What are the main benefits of cloud computing?",
    "15. Explain how binary search works.",
    "16. What is multi-factor authentication?",
    "17. Differentiate between public and private cloud.",
    "18. What is the OSI model and why is it important?",
    "19. List three types of malware and their characteristics.",
    "20. What is the difference between a list and a tuple in Python?"
]

for q in short_questions:
    elements.append(Paragraph(q, normal_style))
    elements.append(Spacer(1, 0.1*inch))

elements.append(PageBreak())

elements.append(Paragraph("<b>Section C: Programming Questions (10 Questions)</b>", heading3_style))

programming_questions = [
    "1. Write a Python program to find the largest of three numbers.",
    "",
    "2. Create a function that reverses a string using a stack.",
    "",
    "3. Write code to implement a simple calculator with +, -, *, / operations.",
    "",
    "4. Implement a queue using Python lists with enqueue and dequeue functions.",
    "",
    "5. Write a program to check if a given string is a palindrome.",
    "",
    "6. Create a function to count vowels in a string.",
    "",
    "7. Write Python code to read a CSV file and display its contents.",
    "",
    "8. Implement bubble sort algorithm in Python.",
    "",
    "9. Write a program that handles division by zero using exception handling.",
    "",
    "10. Create a simple student management system with add, view, and delete functions."
]

for q in programming_questions:
    elements.append(Paragraph(q, normal_style))

elements.append(PageBreak())

elements.append(Paragraph("<b>Section D: Case Study Questions (5 Questions)</b>", heading3_style))

case_questions = [
    "1. Your school wants to migrate its data to the cloud. Recommend a suitable cloud deployment model and justify your choice with three reasons.",
    "",
    "2. A company's network is experiencing slow performance. Describe the steps you would take to troubleshoot the issue using networking tools.",
    "",
    "3. Design a simple cybersecurity policy for a small business. Include at least five essential security practices.",
    "",
    "4. You are developing a queue management system for a hospital. Explain which data structure you would use and why, with implementation details.",
    "",
    "5. Compare and contrast the use of Python and Java for developing a mobile application. Which would you choose and why?"
]

for q in case_questions:
    elements.append(Paragraph(q, normal_style))
    elements.append(Spacer(1, 0.15*inch))

elements.append(PageBreak())

# Additional Resources
elements.append(Paragraph("ADDITIONAL RESOURCES & RECOMMENDATIONS", heading1_style))
elements.append(Spacer(1, 0.2*inch))

elements.append(Paragraph("Recommended Online Platforms", heading2_style))
resources = [
    "<b>For Programming Practice:</b> HackerRank, LeetCode, CodeChef, Exercism",
    "<b>For Cybersecurity:</b> TryHackMe, HackTheBox, Cyber.org, SANS Cyber Aces",
    "<b>For Cloud Learning:</b> AWS Free Tier, Google Cloud Skills Boost, Microsoft Learn",
    "<b>For Networking:</b> Cisco Packet Tracer, GNS3, Wireshark tutorials",
    "<b>For Data Analysis:</b> Kaggle, DataCamp, Google Colab"
]
for resource in resources:
    elements.append(Paragraph(resource, normal_style))

elements.append(Spacer(1, 0.15*inch))
elements.append(Paragraph("Recommended Books", heading2_style))
books = [
    "Python Crash Course by Eric Matthes",
    "Clean Code by Robert C. Martin",
    "The Phoenix Project (DevOps) by Gene Kim",
    "Computer Networking: A Top-Down Approach by Kurose & Ross",
    "The Art of Computer Programming by Donald Knuth (Advanced)"
]
for book in books:
    elements.append(Paragraph(f"• {book}", normal_style))

elements.append(Spacer(1, 0.15*inch))
elements.append(Paragraph("YouTube Channels", heading2_style))
channels = [
    "Programming with Mosh",
    "Corey Schafer (Python)",
    "NetworkChuck (Networking & Security)",
    "freeCodeCamp.org",
    "Fireship (Quick tech tutorials)"
]
for channel in channels:
    elements.append(Paragraph(f"• {channel}", normal_style))

elements.append(Spacer(1, 0.3*inch))
elements.append(Paragraph("Essential Tools to Install", heading2_style))
tools = [
    "Python 3.x (latest stable version)",
    "VS Code with Python extension",
    "Git for version control",
    "Wireshark for network analysis",
    "VirtualBox for virtual machines",
    "Postman for API testing"
]
for tool in tools:
    elements.append(Paragraph(f"✓ {tool}", normal_style))

elements.append(PageBreak())

# Assessment & Grading
elements.append(Paragraph("ASSESSMENT & GRADING CRITERIA", heading1_style))
elements.append(Spacer(1, 0.2*inch))

assessment_data = [
    ['Component', 'Weightage', 'Description'],
    ['Continuous Assessment', '30%', 'Weekly quizzes, class participation, homework'],
    ['Practical Projects', '30%', 'Chapter-wise coding projects and labs'],
    ['Mid-term Examination', '15%', 'Theory and practical exam (Chapters 1-4)'],
    ['Final Examination', '20%', 'Comprehensive theory and practical exam'],
    ['Final Project', '5%', 'Individual capstone project integrating all topics']
]

assessment_table = Table(assessment_data, colWidths=[2*inch, 1.2*inch, 3*inch])
assessment_table.setStyle(TableStyle([
    ('BACKGROUND', (0, 0), (-1, 0), colors.HexColor('#283593')),
    ('TEXTCOLOR', (0, 0), (-1, 0), colors.whitesmoke),
    ('ALIGN', (0, 0), (-1, -1), 'LEFT'),
    ('FONTNAME', (0, 0), (-1, 0), 'Helvetica-Bold'),
    ('FONTSIZE', (0, 0), (-1, -1), 10),
    ('BOTTOMPADDING', (0, 0), (-1, 0), 12),
    ('BACKGROUND', (0, 1), (-1, -1), colors.beige),
    ('GRID', (0, 0), (-1, -1), 1, colors.black),
    ('VALIGN', (0, 0), (-1, -1), 'MIDDLE'),
    ('LEFTPADDING', (0, 0), (-1, -1), 8),
    ('RIGHTPADDING', (0, 0), (-1, -1), 8),
]))
elements.append(assessment_table)

elements.append(Spacer(1, 0.3*inch))
elements.append(Paragraph("Learning Outcomes", heading2_style))
outcomes = [
    "Demonstrate proficiency in Python programming with real-world applications",
    "Understand and apply cybersecurity best practices in daily digital life",
    "Comprehend cloud computing concepts and their business applications",
    "Analyze and troubleshoot network issues using appropriate tools",
    "Implement basic data structures and algorithms",
    "Design secure, efficient, and scalable software solutions",
    "Demonstrate ethical awareness in technology use"
]
for outcome in outcomes:
    elements.append(Paragraph(f"• {outcome}", normal_style))

elements.append(Spacer(1, 0.3*inch))
elements.append(Paragraph("Course Completion Certificate", heading2_style))
elements.append(Paragraph("""
Students who successfully complete this curriculum with a minimum of 60% overall score 
will receive a certificate recognizing their achievement in Technology Fundamentals. 
Outstanding performers (90% and above) will receive Merit certificates.
""", normal_style))

# Footer
elements.append(Spacer(1, 0.5*inch))
footer_text = f"""
<para align=center>
<b>END OF SYLLABUS</b><br/>
<br/>
Prepared for Classes 1-9 Technology Curriculum<br/>
Academic Year 2026-2027<br/>
<br/>
<i>This syllabus is subject to updates based on technological advancements and educational requirements.</i>
</para>
"""
elements.append(Paragraph(footer_text, normal_style))

# Build PDF
doc.build(elements)
print("✓ Comprehensive Technology Syllabus PDF created successfully!")
print("✓ File location: /mnt/user-data/outputs/Technology_Curriculum_Syllabus.pdf")