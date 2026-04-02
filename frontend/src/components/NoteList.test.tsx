import { describe, it, expect, vi } from 'vitest';
import { render, screen } from '@testing-library/react';
import React from 'react';
import NoteList from './NoteList';

describe('NoteList', () => {
  it('renders "No notes found." when notes array is empty', () => {
    render(
      <NoteList
        notes={[]}
        onView={vi.fn()}
        onEdit={vi.fn()}
        onDelete={vi.fn()}
        onArchive={vi.fn()}
        onUnarchive={vi.fn()}
      />
    );
    expect(screen.getByText(/No notes found/i)).toBeDefined();
  });

  it('renders a list of notes', () => {
    const notes = [
      {
        id: 1,
        title: 'Note 1',
        content: 'Content 1',
        archived: false,
        categories: [],
        createdAt: '2024-01-01T00:00:00Z',
        updatedAt: '2024-01-01T00:00:00Z',
      },
      {
        id: 2,
        title: 'Note 2',
        content: 'Content 2',
        archived: true,
        categories: [{ id: 1, name: 'Work' }],
        createdAt: '2024-01-02T00:00:00Z',
        updatedAt: '2024-01-02T00:00:00Z',
      },
    ];
    render(
      <NoteList
        notes={notes}
        onView={vi.fn()}
        onEdit={vi.fn()}
        onDelete={vi.fn()}
        onArchive={vi.fn()}
        onUnarchive={vi.fn()}
      />
    );
    expect(screen.getByText('Note 1')).toBeDefined();
    expect(screen.getByText('Content 1')).toBeDefined();
    expect(screen.getByText('Note 2')).toBeDefined();
    expect(screen.getByText('Content 2')).toBeDefined();
    expect(screen.getByText('Work')).toBeDefined();
  });
});
