export function formatLocalDate(isoString: string): string {
  const normalized = isoString.endsWith('Z') ? isoString : isoString + 'Z';
  const date = new Date(normalized);
  return date.toLocaleString();
}
