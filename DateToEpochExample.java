🔁 Sequential Mode
All rules are executed in the order they appear (based on rule priority or rule set order).

No inference or agenda management.

Once a rule fires, it doesn't re-check other rules unless explicitly designed to.

Performance is predictable, and it’s good for simple or linear rule sets.

✅ Use When:
Rules are independent.

You don’t need conflict resolution or rule chaining.

Example: Simple eligibility checks, validations.

⚡️ Fastpath Mode
Inference-based, like a simplified RETE engine.

Allows rule chaining: firing one rule can activate another.

ODM maintains an agenda and can re-evaluate facts after each rule fires.

More powerful, but a bit slower than sequential for large rule sets.

✅ Use When:
Rules are interdependent.

You need dynamic rule chaining (e.g., one rule’s action affects another).

Example: Complex decision-making, score calculations, dynamic eligibility flows.



Feature | Sequential Mode | Fastpath Mode
Rule Order | Fixed / Manual | Determined by engine
Chaining | ❌ No | ✅ Yes
Re-evaluation | ❌ No | ✅ Yes
Performance | ✅ Faster | ⚠️ Slightly Slower
Use Case | Simple rules | Complex interdependencies
Agenda Management | ❌ None | ✅ Maintains agenda
