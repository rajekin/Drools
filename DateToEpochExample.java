Profile,CPU (Req / Limit),Memory (Req / Limit),Replicas,Max Projects,Concurrent Users,Performance,When to Use
Small,1 vCPU / 1 vCPU,4 GiB / 4 GiB,1,5–8,1–2,Slows down with more than 6 projects or concurrent edits,For development or PoCs or demos or solo rule authoring
Medium,1 vCPU / 1 vCPU,4 GiB / 8 GiB,2,15–20,4–6,Stable and responsive under moderate rule authoring and execution loads,For small to mid production environments or shared authoring teams
Large,2 vCPU / 2 vCPU,4 GiB / 16 GiB,2,25–40+,8–12+,High performance supports large rulebases and multiple concurrent users,For high availability or enterprise scale projects or governance heavy workloads
