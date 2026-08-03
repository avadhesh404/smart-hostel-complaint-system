# Entity Relationship Diagram - Smart Hostel Complaint Management System

## Database Schema Overview

```
┌─────────────┐       ┌─────────────┐       ┌─────────────┐
│    admin    │       │  students   │       │hostel_staff │
├─────────────┤       ├─────────────┤       ├─────────────┤
│ admin_id PK │       │ student_id PK│       │  staff_id PK│
│ username    │       │student_number│       │employee_num │
│ password    │       │ username    │       │ username    │
│ full_name   │       │ password    │       │ password    │
│ email       │       │ full_name   │       │ full_name   │
│ phone       │       │ email       │       │ email       │
│ created_at  │       │ phone       │       │ phone       │
│ updated_at  │       │ block_id FK │───────│ role        │
│ last_login  │       │ room_id FK  │       │ block_id FK │─┐
│ is_active   │       │ course      │       │specializ.  │ │
└─────────────┘       │ year_study  │       │is_available │ │
                       │ created_at  │       │ created_at  │ │
                       │ updated_at  │       │ updated_at  │ │
                       │ last_login  │       │ last_login  │ │
                       │ is_active   │       │ is_active   │ │
                       └─────────────┘       └─────────────┘ │
                              │                     │     │
                              │                     │     │
                              │                     │     │
                              ▼                     │     │
                       ┌─────────────┐             │     │
                       │ complaints  │◄────────────┘     │
                       ├─────────────┤                   │
                       │complaint_id PK│                   │
                       │complaint_num │                   │
                       │student_id FK │                   │
                       │category_id FK│                   │
                       │assigned_staff│                   │
                       │title        │                   │
                       │description  │                   │
                       │block_id FK  │─────────────────────┘
                       │room_id FK   │
                       │priority     │
                       │status       │
                       │submitted_at │
                       │assigned_at  │
                       │in_progress  │
                       │resolved_at  │
                       │closed_at    │
                       │resolution   │
                       │completion   │
                       │created_at   │
                       │updated_at   │
                       └─────────────┘
                              │
                              │
                              │
              ┌───────────────┼───────────────┐
              │               │               │
              ▼               ▼               ▼
    ┌─────────────┐ ┌─────────────┐ ┌─────────────┐
    │complaint_img│ │status_hist │ │ notifications│
    ├─────────────┤ ├─────────────┤ ├─────────────┤
    │image_id PK  │ │history_id PK│ │notif_id PK │
    │complaint_id │ │complaint_id │ │user_id     │
    │image_path   │ │old_status   │ │user_type   │
    │image_name   │ │new_status   │ │complaint_id│
    │uploaded_at  │ │changed_by   │ │title       │
    └─────────────┘ │changed_role │ │message     │
                     │notes        │ │is_read     │
                     │changed_at   │ │created_at  │
                     └─────────────┘ └─────────────┘
                              │
                              │
                              ▼
                     ┌─────────────┐
                     │  feedback   │
                     ├─────────────┤
                     │feedback_id PK│
                     │complaint_id │
                     │student_id   │
                     │rating       │
                     │comments     │
                     │submitted_at │
                     └─────────────┘

┌─────────────┐       ┌─────────────┐       ┌─────────────┐
│ categories  │       │hostel_blocks│       │    rooms    │
├─────────────┤       ├─────────────┤       ├─────────────┤
│category_id PK│       │block_id PK  │       │  room_id PK │
│category_name│       │block_name   │       │ room_number │
│description  │       │block_code   │       │ block_id FK │
│default_prior│       │description  │       │ capacity    │
│est_resol_hr │       │total_rooms  │       │current_occ │
│is_active    │       │warden_name  │       │ floor_num   │
│created_at   │       │warden_phone │       │ room_type   │
│updated_at   │       │created_at   │       │ is_active   │
└─────────────┘       │updated_at   │       │ created_at  │
                       │is_active    │       │ updated_at  │
                       └─────────────┘       └─────────────┘
                              │                     │
                              │                     │
                              └─────────────────────┘

┌─────────────┐
│ audit_logs  │
├─────────────┤
│log_id PK    │
│user_id      │
│user_type    │
│action       │
│table_name   │
│record_id    │
│old_values    │
│new_values    │
│ip_address   │
│user_agent   │
│created_at   │
└─────────────┘
```

## Relationships

### One-to-Many Relationships
- **admin** → **audit_logs** (Admin creates audit logs)
- **students** → **complaints** (Student submits complaints)
- **students** → **feedback** (Student provides feedback)
- **hostel_staff** → **complaints** (Staff handles complaints)
- **hostel_blocks** → **rooms** (Block contains rooms)
- **hostel_blocks** → **students** (Block houses students)
- **hostel_blocks** → **hostel_staff** (Block has staff)
- **categories** → **complaints** (Category contains complaints)
- **rooms** → **students** (Room houses students)
- **complaints** → **complaint_images** (Complaint has images)
- **complaints** → **complaint_status_history** (Complaint has status history)
- **complaints** → **feedback** (Complaint receives feedback)
- **complaints** → **notifications** (Complaint generates notifications)

### Many-to-One Relationships
- **complaints** → **students** (Complaint belongs to student)
- **complaints** → **hostel_staff** (Complaint assigned to staff)
- **complaints** → **categories** (Complaint belongs to category)
- **complaints** → **hostel_blocks** (Complaint in block)
- **complaints** → **rooms** (Complaint in room)
- **students** → **hostel_blocks** (Student in block)
- **students** → **rooms** (Student in room)
- **hostel_staff** → **hostel_blocks** (Staff assigned to block)
- **rooms** → **hostel_blocks** (Room in block)

### Self-Referencing
- **complaint_status_history** tracks status changes within complaints

## Key Constraints

### Primary Keys (PK)
- All tables have auto-increment integer primary keys

### Foreign Keys (FK)
- `students.block_id` → `hostel_blocks.block_id`
- `students.room_id` → `rooms.room_id`
- `hostel_staff.block_id` → `hostel_blocks.block_id`
- `complaints.student_id` → `students.student_id`
- `complaints.category_id` → `categories.category_id`
- `complaints.assigned_staff_id` → `hostel_staff.staff_id`
- `complaints.block_id` → `hostel_blocks.block_id`
- `complaints.room_id` → `rooms.room_id`
- `complaint_images.complaint_id` → `complaints.complaint_id`
- `complaint_status_history.complaint_id` → `complaints.complaint_id`
- `notifications.complaint_id` → `complaints.complaint_id`
- `feedback.complaint_id` → `complaints.complaint_id`
- `feedback.student_id` → `students.student_id`
- `rooms.block_id` → `hostel_blocks.block_id`

### Unique Constraints
- `admin.username`
- `admin.email`
- `students.student_number`
- `students.username`
- `students.email`
- `hostel_staff.employee_number`
- `hostel_staff.username`
- `hostel_staff.email`
- `categories.category_name`
- `hostel_blocks.block_code`
- `rooms.room_number` + `rooms.block_id` (composite)
- `complaints.complaint_number`
- `feedback.complaint_id` + `feedback.student_id` (composite)

### Indexes
- All foreign keys have indexes
- All unique constraints have indexes
- Additional indexes on frequently queried columns:
  - `complaints(status, priority, submitted_at, resolved_at)`
  - `complaint_status_history(complaint_id, changed_at)`
  - `notifications(user_id, user_type, is_read, created_at)`
  - `audit_logs(user_id, user_type, action, table_name, created_at)`

## Cascade Rules

### ON DELETE CASCADE
- `complaint_images` when complaint is deleted
- `complaint_status_history` when complaint is deleted
- `complaints` when student is deleted
- `feedback` when complaint or student is deleted

### ON DELETE SET NULL
- `students.block_id` when block is deleted
- `students.room_id` when room is deleted
- `hostel_staff.block_id` when block is deleted
- `complaints.assigned_staff_id` when staff is deleted

### ON DELETE RESTRICT
- `complaints.category_id` (cannot delete category with complaints)
- `complaints.block_id` (cannot delete block with complaints)
- `complaints.room_id` (cannot delete room with complaints)
- `hostel_blocks` when `rooms` reference it
